package kallas.zubrzycki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Scanner scanner = new Scanner(System.in);
    private Thread receiveThread;
    private boolean isConnected = false;

    public void startConnection(final String ip, final int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            receiveThread = new Thread(this::receiveMessages);
            receiveThread.start();

            isConnected = true;
        } catch (IOException e) {
            System.err.println("Couldn't connect to the server");
        }
    }


    private void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
            receiveThread.interrupt();
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Error when closing client connection");
            e.printStackTrace();
        }
    }

    private void receiveMessages() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.trim().equals("The game is over")) {
                    System.out.println("Thank you for playing");
                    stopConnection();
                }
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error when receiving messages from the server");
            e.printStackTrace();
        }
    }

    public void loop() {
        if (!isConnected) {
            return;
        }

        while (true) {
            String input = scanner.nextLine();
            out.println(input);
        }
    }


    public void addBot() {
        if (!isConnected) {
            return;
        }

        while(true) {
            try {
                sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Random moveGenerator = new Random();
            if (moveGenerator.nextDouble() < 0.05) { // 8% chance to pass
                out.println("pass");
            } else {
                int x = moveGenerator.nextInt(GameManager.BOARD_SIZE) + 1;
                int y = moveGenerator.nextInt(GameManager.BOARD_SIZE) + 1;
                out.println("go " + x + " " + y);
            }
        }
    }
}
