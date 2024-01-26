package kallas.zubrzycki;

import java.sql.*;

public class SQLLiteJDBC {
    Connection connection = null;

    public void initialize() throws SQLException {
        String url = "jdbc:sqlite:database.db";
        connection = DriverManager.getConnection(url);
    }

    public void createNewGame() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS go (game_id integer PRIMARY KEY, move_number integer, move_text VARCHAR(8))";
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
    }

    public void insertNewMove(int gameId, int moveNumber, String moveText) throws SQLException {
        String sql = "INSERT INTO go (game_id, move_number, move_text) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, gameId);
            pstmt.setInt(2, moveNumber);
            pstmt.setString(3, moveText);
            pstmt.executeUpdate();
        }
    }


    public static void main(String[] args) throws SQLException {
        SQLLiteJDBC db = new SQLLiteJDBC();
        db.initialize();
        db.createNewGame();
        db.insertNewMove(1, 1, "pass");
    }
}
