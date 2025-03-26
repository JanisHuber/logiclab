package ch.janishuber.logiclab.adapter.persistence.select;

import ch.janishuber.logiclab.adapter.persistence.DatabaseConnection;

import java.io.IOException;
import java.sql.*;

public class GameOutput {

    public static String getGameStatus(int gameId) throws IOException {
        DatabaseConnection dbCon = new DatabaseConnection();

        try (Connection conn = DriverManager.getConnection(dbCon.getUrl(), dbCon.getUser(), dbCon.getPassword())) {
            String insertSQL = "SELECT gameStatus FROM mastermind WHERE gameId = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setInt(1, gameId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getString("gameStatus");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
