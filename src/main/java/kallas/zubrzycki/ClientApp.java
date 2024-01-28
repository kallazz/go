package kallas.zubrzycki;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("1. Join Game\n2. View Past Games\n3. Join as a bot\n4. Exit");

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
                replayGame(replayGameId);

            }
            catch (SQLException ex) {
                System.out.println("Something went wrong with the database");
                ex.printStackTrace();
            }
        } else if(input.equals("3")){
            client.startConnection("127.0.0.1", 6666);
            client.addBot();
        } else if(input.equals("4")){
            scanner.close();
            System.exit(0);
        }
    }

    public static void replayGame(int gameId) throws SQLException {
        SQLLiteJDBC db = new SQLLiteJDBC("jdbc:sqlite:database.db");
        int i = 1;
        Statement statement = db.getConnection().createStatement();

        while (true) {
            String query = "SELECT move_text FROM moves WHERE game_id=" + gameId + " AND move_number=" + i;
            ResultSet rs = statement.executeQuery(query);

            if (!rs.next()) {  // No more moves found
                break;
            }
            if(i%2 == 1){
                System.out.println(i + ". BLACK: " + rs.getString("move_text"));
            } else {
                System.out.println(i + ". WHITE: " + rs.getString("move_text"));
            }

            // Replay the move using moveText


            i++;
        }

        statement.close();
    }
}
