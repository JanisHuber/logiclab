package database.acceptancetest;

import ch.janishuber.logiclab.adapter.persistence.insert.GameInput;
import ch.janishuber.logiclab.adapter.persistence.insert.GuessInput;
import ch.janishuber.logiclab.adapter.persistence.select.GuessOutput;
import ch.janishuber.logiclab.domain.Guess;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateGameAndInsertGuessesTest {

    @Test
    void test() {
        Optional<Integer> gameId = GameInput.createNewGame("1234");
        if (gameId.isEmpty()) {
            throw new RuntimeException("GameId is not found");
        }

        assertThat(gameId.get())
                .as("GameId should be greater than 0")
                .isNotNull();

        boolean hasInserted = GuessInput.inputGuess(gameId.get(), new Guess("4321", 0, 4));
        assertThat(hasInserted)
                .as("Guess should be inserted")
                .isTrue();

        List<Guess> guesses = GuessOutput.getGuesses(gameId.get());
        assertThat(guesses.size())
                .as("Number of guesses")
                .isEqualTo(1);
    }
}
