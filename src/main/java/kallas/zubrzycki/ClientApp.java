package kallas.zubrzycki;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class ClientApp {

    private final Client client = new Client();
    private final Scanner scanner = new Scanner(System.in);

    public ClientApp() {}

    public void run() {
        System.out.println("1. Join Game\n2. View Past Games\n3. Join as a bot\n4. Exit");

        String input = scanner.nextLine();
        while (!input.equals("1") && !input.equals("2") && !input.equals("3") && !input.equals("4")) {
            System.out.println("Incorrect input. The only allowed options are 1, 2, 3, 4.");
            input = scanner.nextLine();
        }

        switch (input) {
            case "1":
                joinGame();
                break;
            case "2":
                viewPastGames();
                break;
            case "3":
                joinAsBot();
                break;
            case "4":
                exitApp();
                break;
        }
    }

    private void joinGame() {
        client.startConnection("127.0.0.1", 6666);
        client.loop();
    }

    private void viewPastGames() {
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
    }

    private void joinAsBot() {
        client.startConnection("127.0.0.1", 6666);
        client.addBot();
    }

    private void exitApp() {
        scanner.close();
        System.exit(0);
    }

    public void replayGame(int gameId) throws SQLException {

        SQLLiteJDBC db = new SQLLiteJDBC("jdbc:sqlite:database.db");
        int i = 1;
        Statement statement = db.getConnection().createStatement();

        // Replay the move using moveText
        Board board = Board.getInstance();
        GameManager gameManager = new GameManager();
        board.initialize(19, gameManager);
        gameManager.initializeGame(0, 1);
        while (true) {
            String query = "SELECT move_text FROM moves WHERE game_id=" + gameId + " AND move_number=" + i;
            ResultSet rs = statement.executeQuery(query);

            if (!rs.next()) {  // No more moves found
                break;
            }
            String move_text = rs.getString("move_text");




            String input_words[] = move_text.split(" ");
            if(input_words.length == 3){
                int x = Integer.parseInt(input_words[1]);
                int y = Integer.parseInt(input_words[2]);
                if(i % 2 == 1){
                    board.performMove(x, y, EPointColor.BLACK);
                } else {
                    board.performMove(x, y, EPointColor.WHITE);
                }
            }
            System.out.println(board.getBoardView());
            try {
                sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            i++;
        }
        statement.close();
    }

    public static void main(String[] args) {
        ClientApp app = new ClientApp();
        app.run();
    }
}