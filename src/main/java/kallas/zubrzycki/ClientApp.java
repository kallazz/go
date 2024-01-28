package kallas.zubrzycki;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        Client client = new Client();
        if (input.equals("1")) {

            client.startConnection("127.0.0.1", 6666);
            client.loop();
        } else if (input.equals("2")) {
            System.out.println("Here are all previous games:");
            try {
                SQLLiteJDBC db = new SQLLiteJDBC("jdbc:sqlite:database.db");
                String query = "SELECT COUNT(*) FROM games";
                String query2 = "SELECT * FROM games";
                Statement statement = db.getConnection().createStatement();
                ResultSet rs = statement.executeQuery(query);
                int rowCount = rs.getInt(1);
                rs.close();

                ResultSet rs2 = statement.executeQuery(query2);

                int i = 1;
                while(rs2.next()){
                    System.out.println(i + " - ID: " + rs2.getString("game_id") + " Winner: " + rs2.getString("winner") + " Date: " + rs2.getDate("date"));
                    i++;
                }
                System.out.println("Which game would you like to replay?");
                int replayGameId = scanner.nextInt();
                client.replayGame(replayGameId);

            }
            catch (SQLException ex) {
                System.out.println("Something went wrong with the database");
                ex.printStackTrace();
            }
        } else {
            scanner.close();
            System.exit(0);
        }
    }
}
