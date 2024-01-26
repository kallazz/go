package kallas.zubrzycki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Scanner scanner = new Scanner(System.in);

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Error when initializing client connection");
            e.printStackTrace();
        }

    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error when closing client connection");
            e.printStackTrace();
        }
    }

    public void loop() {
        while (true) {
            String input = scanner.nextLine();
            String output = sendMessage(input);
            System.out.flush();
            System.out.println(output);
        }
    }

    public String sendMessage(String msg) {
        try {
            out.println(msg);

            String output = "";
            String line;
            while (!((line = in.readLine()).equals("\u0004"))) {
                output += line + '\n';
            }
            return output;
        } catch (Exception e) {
            return null;
        }
    }
}