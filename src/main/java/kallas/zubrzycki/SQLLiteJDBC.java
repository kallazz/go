package kallas.zubrzycki;

import java.sql.*;

public class SQLLiteJDBC {
    private Connection connection = null;

    public Connection getConnection() {
        return connection;
    }

    public void initialize() throws SQLException {
        String url = "jdbc:sqlite:database.db";
        connection = DriverManager.getConnection(url);
    }

    public void createNewGame() throws SQLException {
        String query1 = "CREATE TABLE IF NOT EXISTS moves (game_id integer, move_number integer, move_text VARCHAR(8))";
        String query2 = "CREATE TABLE IF NOT EXISTS games (game_id integer PRIMARY KEY, winner VARCHAR(5), date DATETIME";
        Statement stmt = connection.createStatement();
        stmt.execute(query1);
        stmt.execute(query2);
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

    SQLLiteJDBC(String url) throws SQLException {
        this.connection = DriverManager.getConnection(url);
    }


    public static void main(String[] args) throws SQLException {
        SQLLiteJDBC db = new SQLLiteJDBC("jdbc:sqlite:database.db");
        db.initialize();
        db.createNewGame();
    }
}
