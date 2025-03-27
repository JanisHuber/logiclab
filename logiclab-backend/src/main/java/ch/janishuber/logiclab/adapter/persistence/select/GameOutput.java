package ch.janishuber.logiclab.adapter.persistence.select;

import ch.janishuber.logiclab.adapter.persistence.DatabaseConnection;
import ch.janishuber.logiclab.adapter.rest.dto.GameDto;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class GameOutput {

    public static Optional<GameDto> getGameState(int gameId) {
        Optional<String> gameState = getGameStatus(gameId);
        return gameState.map(s -> new GameDto(gameId, s, GuessOutput.getGuesses(gameId).size()));
    }

    private static Optional<String> getGameStatus(int gameId) {
        try {
            DatabaseConnection dbCon = new DatabaseConnection();

            try (Connection conn = DriverManager.getConnection(dbCon.getUrl(), dbCon.getUser(), dbCon.getPassword())) {
                String insertSQL = "SELECT gameStatus FROM mastermind WHERE gameId = ?";

                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                    pstmt.setInt(1, gameId);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        return Optional.ofNullable(rs.getString("gameStatus"));
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

    public static Optional<String> getMastermindNumber(int gameId) {
        try {
            DatabaseConnection dbCon = new DatabaseConnection();

            try (Connection conn = DriverManager.getConnection(dbCon.getUrl(), dbCon.getUser(), dbCon.getPassword())) {
                String insertSQL = "SELECT masterMindNumber FROM mastermind WHERE gameId = ?";

                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                    pstmt.setInt(1, gameId);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        return Optional.ofNullable(rs.getString("masterMindNumber"));
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
