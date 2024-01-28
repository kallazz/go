package kallas.zubrzycki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
// TODO: remove na disconnecie, jakiś makemove w gamemanager(mutex?)  
public class Server {
    private ServerSocket serverSocket;
    private List<ClientHandler> clientHandlers = new ArrayList<>();

    private int numberOfPlayers = 0;
    protected GameManager gameManager = new GameManager();
    protected boolean isGameActive = false;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
            while (numberOfPlayers < 2) {
                Socket clientSocket = serverSocket.accept(); // Wait for a new connection

                ClientHandler clientHandler = new ClientHandler(clientSocket, numberOfPlayers);
                clientHandlers.add(clientHandler);
                clientHandler.start();

                System.out.println("Client connected to the server");
                numberOfPlayers++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("2 players connected. The game is starting");
        isGameActive = true;

        ClientHandler firstClientHandler = clientHandlers.get(0);
        ClientHandler secondClientHandler = clientHandlers.get(1);
        int firstPlayerId = clientHandlers.get(0).getPlayerId();
        int secondPlayerId = clientHandlers.get(1).getPlayerId();
        gameManager.attachObserver(new GameManagerObserver(gameManager, firstClientHandler, firstPlayerId));
        gameManager.attachObserver(new GameManagerObserver(gameManager, secondClientHandler, secondPlayerId));
        gameManager.initializeGame(firstPlayerId, secondPlayerId);
        firstClientHandler.sendMessage(gameManager.getBoard(firstPlayerId));
        secondClientHandler.sendMessage(gameManager.getBoard(secondPlayerId));

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
        firstClientHandler.sendMessage(results);
        secondClientHandler.sendMessage(results);
        firstClientHandler.sendMessage("The game is over");
        secondClientHandler.sendMessage("The game is over");
        System.out.println("The game is finished!");
        stop();
    }

    public void finishGameFaster(int stoppedPlayerId) {
        isGameActive = false;
        System.out.println("One of the clients disconnected. The game is over.");
        addGameToDb();
        if (clientHandlers.get(0).getPlayerId() == stoppedPlayerId) {
            clientHandlers.get(1).sendMessage("Another player disconnected");
            clientHandlers.get(1).sendMessage("The game is over");
        } else {
            clientHandlers.get(0).sendMessage("Another player disconnected");
            clientHandlers.get(0).sendMessage("The game is over");
        }

        stop();
    }

    private void addGameToDb() {
        try {
            gameManager.addGameToDatabase();
            System.out.println("Game added to the database");
        } catch (SQLException e) {
            System.out.println("Game couldn't get added to the database");
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


    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private int id;
        private BufferedReader userInput;
        private PrintWriter userOutput;

        public ClientHandler(Socket socket, int id) {
            this.clientSocket = socket;
            this.id = id;
        }

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            this.id = 999; // BOT
        }

        public int getPlayerId() {
            return this.id;
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
                        userOutput.println("The game hasn't started yet!\u0004");
                    } else {
                        gameManager.makeMove(inputLine, id);
                    }
                }

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