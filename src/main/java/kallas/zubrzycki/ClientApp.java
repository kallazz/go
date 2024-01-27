package kallas.zubrzycki;

import java.sql.SQLException;
import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("1. Join Game\n2. View Past Games\n3. Exit");

        String input = scanner.nextLine();
        while (!input.equals("1") && !input.equals("2") && !input.equals("3")) {
            System.out.println("Incorrect input. The only allowed options are 1, 2, 3.");
            input = scanner.nextLine();
        }

        if (input.equals("1")) {
            Client client = new Client();
            client.startConnection("127.0.0.1", 6666);
            client.loop();
        } else if (input.equals("2")) {
            System.out.println("Here are all previous games:");
            try {
                SQLLiteJDBC db = new SQLLiteJDBC("jdbc:sqlite:database.db");
            }
            catch (SQLException ex) {
                System.out.println("Something went wrong with the database");
            }
        } else {
            scanner.close();
            System.exit(0);
        }
    }
}
