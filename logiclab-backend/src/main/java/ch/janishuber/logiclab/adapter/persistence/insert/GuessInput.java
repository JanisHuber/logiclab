package ch.janishuber.logiclab.adapter.persistence.insert;

import ch.janishuber.logiclab.adapter.persistence.DatabaseConnection;
import ch.janishuber.logiclab.domain.Guess;

import java.io.IOException;
import java.sql.*;

public class GuessInput {

    public static boolean inputGuess(int gameId, Guess guess) {
        try {
            DatabaseConnection dbCon = new DatabaseConnection();

            try (Connection conn = DriverManager.getConnection(dbCon.getUrl(), dbCon.getUser(), dbCon.getPassword())) {
                String insertSQL = "INSERT INTO playerGuesses (guess, correctPosition, correctNumber) VALUES (?, ?, ?)";

                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    pstmt.setString(1, guess.guess());
                    pstmt.setInt(2, guess.correctPosition());
                    pstmt.setInt(3, guess.correctNumber());
                    pstmt.executeUpdate();

                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            return createRelation(gameId, rs.getInt(1));
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean createRelation(int gameId, int guessId) {
        try {
            DatabaseConnection dbCon = new DatabaseConnection();

            try (Connection conn = DriverManager.getConnection(dbCon.getUrl(), dbCon.getUser(), dbCon.getPassword())) {
                String insertSQL = "INSERT INTO guesstogame (gameId, guessId) VALUES (?, ?)";

                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                    pstmt.setInt(1, gameId);
                    pstmt.setInt(2, guessId);

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
}
