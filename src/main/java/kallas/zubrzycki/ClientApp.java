package kallas.zubrzycki;

import java.sql.SQLException;
import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) throws SQLException {
        Client client = new Client();
        Scanner scanner = new Scanner(System.in);
        int input;
        do {
            System.out.println("1 - Join game. 2 - View Past Games");
            input = scanner.nextInt();
        } while(input == 1 || input == 0);
        if(input == 1){
            client.startConnection("127.0.0.1", 6666);
            client.loop();
        } else {
            System.out.println("Here are all previous games:");
            SQLLiteJDBC db = new SQLLiteJDBC("jdbc:sqlite:database.db");

        }


    }
}
