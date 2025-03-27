package ch.janishuber.logiclab.adapter.persistence.insert;

import ch.janishuber.logiclab.adapter.persistence.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class GameInput {

    public static boolean updateGameStatus(int gameId, String gameStatus) {
        try {
            DatabaseConnection dbCon = new DatabaseConnection();

            try (Connection conn = DriverManager.getConnection(dbCon.getUrl(), dbCon.getUser(), dbCon.getPassword())) {
                String insertSQL = "UPDATE mastermind SET gameStatus = ? WHERE gameId = ?";

                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                    pstmt.setString(1, gameStatus);
                    pstmt.setInt(2, gameId);
                    return pstmt.executeUpdate() > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Optional<Integer> createNewGame(String generatedMasterMindNumber) {
        try {
            DatabaseConnection dbCon = new DatabaseConnection();

            try (Connection conn = DriverManager.getConnection(dbCon.getUrl(), dbCon.getUser(), dbCon.getPassword())) {
                String insertSQL = "INSERT INTO mastermind (masterMindNumber, gameStatus) VALUES (?, ?)";

                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    pstmt.setString(1, generatedMasterMindNumber);
                    pstmt.setString(2, "started");
                    pstmt.executeUpdate();

                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            return Optional.ofNullable(rs.getInt(1));
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
