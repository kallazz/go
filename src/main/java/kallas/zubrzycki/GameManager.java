package kallas.zubrzycki;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class GameManager implements IGameManager {
    private static int BOARD_SIZE = 19;

    private Board board;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private boolean isPassedPlayer1 = false;
    private boolean isPassedPlayer2 = false;
    private boolean didBothPlayersPass = false;

    private SQLLiteJDBC db;
    private int game_id;
    private int current_turn;

    private List<IGameManagerObserver> observers = new ArrayList<IGameManagerObserver>();
    public SQLLiteJDBC getDatabase(){
        return this.db;
    }

    int obtainGameId(SQLLiteJDBC db) throws SQLException {

        Statement statement = db.getConnection().createStatement();
        String query = "SELECT MAX(game_id) AS max_value FROM games";
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            int highestValue = resultSet.getInt("max_value");
            return highestValue + 1;
        }
        return 1;
    }

    @Override
    public void initializeGame(int player1Id, int player2Id) {

        current_turn = 0;

        board = Board.getInstance();
        board.initialize(BOARD_SIZE, this);
        player1 = new Player(EPointColor.BLACK, player1Id);
        player2 = new Player(EPointColor.WHITE, player2Id);
        currentPlayer = player1;

        try {
            db = new SQLLiteJDBC("jdbc:sqlite:database.db");
            this.game_id = obtainGameId(db);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public synchronized void makeMove(String input, int playerId) {
        // Quit if it's not this player's turn
        if (playerId != currentPlayer.getId()) {
            board.addErrorMessage("This is not your turn!", playerId);
            notifyObservers();
            return;
        }

        // Quit if input is incorrect
        if (!parseInput(input, playerId)) {
            notifyObservers();
            return;
        }

        // Handle correct input
        if (!input.equals("pass")) {
            final int x = Integer.parseInt(input.split(" ")[1]);
            final int y = Integer.parseInt(input.split(" ")[2]);

            if (board.checkMove(x, y, currentPlayer.getColor(), currentPlayer.getId())) {


                board.performMove(x, y, currentPlayer.getColor());
                try {
                    db.insertNewMove(game_id, current_turn, input);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                // Reset this player's pass status
                if (currentPlayer == player1) {
                    isPassedPlayer1 = false;
                } else {
                    isPassedPlayer2 = false;
                }
            } else {
                board.addErrorMessage("Wrong move - you lose your turn", playerId);
            }
        } else {

            try {
                db.insertNewMove(game_id, current_turn, input);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (currentPlayer == player1) {
                isPassedPlayer1 = true;
            } else {
                isPassedPlayer2 = true;
            }
        }

        if (isPassedPlayer1 && isPassedPlayer2) {
            countScore();
            notifyObservers();
            didBothPlayersPass = true;
            return;
        }

        currentPlayer = (player1 == currentPlayer) ? player2 : player1;
        notifyObservers();
    }

    private boolean parseInput(String input, int playerId) {
        if (input.equals("pass")) {
            return true;
        } else {
            final String[] words = input.split(" ");

            if (words[0].equals("go")) {
                try {
                    final int x = Integer.parseInt(words[1]);
                    final int y = Integer.parseInt(words[2]);

                    if (x < 0 || x > BOARD_SIZE || y < 0 || y > BOARD_SIZE) {
                        board.addErrorMessage("x and y should be in <1, " + BOARD_SIZE + ">", playerId);
                        return false;
                    } else {
                        return true;
                    }
                } catch (Exception ex) {
                    board.addErrorMessage("Correct format: go <x> <y>", playerId);
                    return false;
                }
            }
        }
        board.addErrorMessage("Allowed actions are: 'go <x> <y>' or 'pass'", playerId);
        return false;
    }

    @Override
    public void countScore() {

    }

    public boolean isGameFinished() {
        return didBothPlayersPass;
    }

    public String getBoard(int playerId) {
        EPointColor playerColor = (player1.getId() == playerId) ? player1.getColor() : player2.getColor();
        return board.getBoardView(playerId, playerColor, currentPlayer.getColor());
    }

    // *************************************** Observers ***************************************

    public void attachObserver(IGameManagerObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers() {
        for (IGameManagerObserver observer : observers) {
            observer.update();
        }
    }
}
