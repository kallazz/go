package kallas.zubrzycki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

// TODO: remove na disconnecie, jakiś makemove w gamemanager(mutex?)  
public class Server {
    private ServerSocket serverSocket;
    private ClientHandler[] clientHandlers = {null, null}; // First handler has id=0 and second handler id=1

    private int numberOfPlayers = 0;
    protected GameManager gameManager = new GameManager();
    protected boolean isGameActive = false;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
            waitForPlayers(port);
        } catch (IOException e) {
            e.printStackTrace();
        }


        gameManager.attachObserver(new GameManagerObserver(gameManager, clientHandlers[0], 0));
        gameManager.attachObserver(new GameManagerObserver(gameManager, clientHandlers[1], 1));
        gameManager.initializeGame(0, 1);
        clientHandlers[0].sendMessage(gameManager.getBoard(0));
        clientHandlers[1].sendMessage(gameManager.getBoard(1));

        while (!gameManager.isGameFinished()) {
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        // WHAT HAPPENS AFTER THE GAME
        // Add game to database
        isGameActive = false;
        addGameToDb();
        gameManager.countScore();
        String winner = gameManager.getWinner();
        String results = "The final score is BLACK - " + gameManager.getPlayer1().getScore() + " : " + gameManager.getPlayer2().getScore() + " - WHITE";
        clientHandlers[0].sendMessage(results);
        clientHandlers[1].sendMessage(results);
        clientHandlers[0].sendMessage("The game is over");
        clientHandlers[1].sendMessage("The game is over");
        System.out.println("The game is finished!");
        stop();
    }

    public void finishGameFaster(int stoppedPlayerId) {
        isGameActive = false;
        System.out.println("One of the clients disconnected. The game is over.");
        addGameToDb();
        if (clientHandlers[0].getPlayerId() == stoppedPlayerId) {
            clientHandlers[1].sendMessage("Another player disconnected");
            clientHandlers[1].sendMessage("The game is over");
        } else {
            clientHandlers[0].sendMessage("Another player disconnected");
            clientHandlers[0].sendMessage("The game is over");
        }

        stop();
    }

    private void waitForPlayers(int port) throws IOException {
        while (true) {
            while (numberOfPlayers < 2) {
                Socket clientSocket = serverSocket.accept(); // Wait for a new connection

                addAndStartClientHandler(clientSocket);

                System.out.println("Client connected to the server");
                numberOfPlayers++;
            }

            if (clientHandlers[0].isActive() && clientHandlers[1].isActive()) {
                break;
            }

            if (!clientHandlers[0].isActive()) {
                numberOfPlayers--;
                clientHandlers[0] = null;
            }
            if (!clientHandlers[1].isActive()) {
                numberOfPlayers--;
                clientHandlers[1] = null;
            }
        }

        System.out.println("2 players connected. The game is starting");
        isGameActive = true;

        // To make sure that every type of disconnection is handled
        if (!clientHandlers[0].isActive()) {
            finishGameFaster(0);
        }
        if (!clientHandlers[1].isActive()) {
            finishGameFaster(1);
        }
    }

    private void stop() {
        try {
            System.out.println("Server successfully stopped");
            serverSocket.close();
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Error when closing the server socket");
            e.printStackTrace();
        }

    }

    private void addGameToDb() {
        try {
            gameManager.addGameToDatabase();
            System.out.println("Game added to the database");
        } catch (SQLException e) {
            System.out.println("Game couldn't get added to the database");
        }
    }

    private void addAndStartClientHandler(Socket clientSocket) {
        if (clientHandlers[0] == null) {
            clientHandlers[0] = new ClientHandler(clientSocket, 0);
            clientHandlers[0].start();
        } else {
            clientHandlers[1] = new ClientHandler(clientSocket, 1);
            clientHandlers[1].start();
        }
    }


    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private BufferedReader userInput;
        private PrintWriter userOutput;
        private int id;
        private boolean isClientConnected = false;

        public ClientHandler(Socket socket, int id) {
            this.clientSocket = socket;
            this.id = id;
            this.isClientConnected = true;
        }

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            this.id = 999; // BOT
            this.isClientConnected = true;
        }

        public int getPlayerId() {
            return id;
        }

        public boolean isActive() {
            return isClientConnected;
        }

        public void sendMessage(String message) {
            userOutput.println(message + "\u0004");
        }

        public void run() {
            try {
                userInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                userOutput = new PrintWriter(clientSocket.getOutputStream(), true);

                sendMessage("Welcome to the server!");

                String inputLine;
                while ((inputLine = userInput.readLine()) != null) {
                    if (!isGameActive) {
                        sendMessage("The game hasn't started yet!");
                    } else {
                        gameManager.makeMove(inputLine, id);
                    }
                }
                System.out.println("Client disconnected from the server");
                clientSocket.close();
                isClientConnected = false;

                if (isGameActive) {
                    finishGameFaster(id);
                }
            } catch (IOException e) {
                System.out.println("Error when running ClientHandler");
                e.printStackTrace();
            }
        }
    }


    private class GameManagerObserver extends IGameManagerObserver {
        private GameManager gameManager;
        private ClientHandler clientHandler;

        public GameManagerObserver(GameManager gameManager, ClientHandler clientHandler, int playerId) {
            this.gameManager = gameManager;
            this.clientHandler = clientHandler;
        }

        @Override
        public void update() {
            String board = gameManager.getBoard(clientHandler.getPlayerId());
            clientHandler.sendMessage(board);
        }
    }
}