package ch.janishuber.logiclab.adapter.persistence.select;

import ch.janishuber.logiclab.adapter.persistence.DatabaseConnection;
import ch.janishuber.logiclab.domain.Guess;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuessOutput {

    public static List<Guess> getGuesses(int gameId) throws IOException {
        List<Guess> outputGuesses = new ArrayList<>();
        DatabaseConnection dbCon = new DatabaseConnection();

        try (Connection conn = DriverManager.getConnection(dbCon.getUrl(), dbCon.getUser(), dbCon.getPassword())) {
            String insertSQL = "SELECT * FROM playerguesses JOIN guesstogame ON playerguesses.guessId = guesstogame.guessId WHERE guesstogame.gameId = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setInt(1, gameId);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    Guess guess = new Guess(rs.getString("guess"), rs.getInt("correctPosition"), rs.getInt("correctNumber"));
                    outputGuesses.add(guess);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return outputGuesses;
    }
}
