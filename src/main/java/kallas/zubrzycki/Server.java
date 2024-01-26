package kallas.zubrzycki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
// TODO: remove na disconnecie, jakiś makemove w gamemanager(mutex?)  
public class Server {
    private ServerSocket serverSocket;
    private List<ClientHandler> clientHandlers = new ArrayList<>();

    private int numberOfPlayers = 0;
    protected GameManager gameManager = new GameManager();
    protected boolean isGameStarted = false;

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
        } finally {
            stop();
        }

        System.out.println("2 players connected. The game is starting");
        isGameStarted = true;
        gameManager.initializeGame(clientHandlers.get(0).getClientId(), clientHandlers.get(1).getClientId());
        while (true) {
            // TODO: Finish
        }
    }

    public void stop() {
        try {
            System.out.println("Server successfully stopped");
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Error when closing the server socket");
            e.printStackTrace();
        }

    }


    private class ClientHandler extends Thread {
        private Socket clientSocket;

        private int id;

        public ClientHandler(Socket socket, int id) {
            this.clientSocket = socket;
            this.id = id;
        }

        public int getClientId() {
            return this.id;
        }

        public void run() {
            try {
                BufferedReader userInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter userOutput = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = userInput.readLine()) != null) {
                    if (!isGameStarted) {
                        System.out.println("fuk");
                        userOutput.println("The game hasn't started yet!\n\u0004"); // TODO: Find a normal way to do this
                    } else {
                        String board = gameManager.makeMove(inputLine, id);
                        userOutput.println(board + "\n\u0004");
                    }

                }

                
            } catch (IOException e) {
                System.out.println("Error when running ClientHandler");
                e.printStackTrace();
            }
        }
    }
}