package domain.unittest;

import ch.janishuber.logiclab.adapter.rest.dto.GuessDto;
import ch.janishuber.logiclab.domain.Guess;
import ch.janishuber.logiclab.domain.ValidateGuess;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class GuessValidationTest {

    @Test
    void testGuessValidation() {
        Optional<Guess> guess = ValidateGuess.validateGuess(10, new GuessDto("1234"));
        if (guess.isEmpty()) {
            throw new RuntimeException("Guess is empty");
        }

        assertThat(guess.get().correctPosition())
                .as("Correct Positions should be equal to 4")
                .isEqualTo(4);
        assertThat(guess.get().correctNumber())
                .as("Numbers should be equal to 4")
                .isEqualTo(4);
    }

    @Test
    void testGuessValidationWhenDoubleLetters() {
        Optional<Guess> guess = ValidateGuess.validateGuess(11, new GuessDto("8877"));
        if (guess.isEmpty()) {
            throw new RuntimeException("Guess is empty");
        }

        assertThat(guess.get().correctPosition()).isEqualTo(4);
        assertThat(guess.get().correctNumber()).isEqualTo(4);

        Optional<Guess> guess2 = ValidateGuess.validateGuess(11, new GuessDto("7788"));
        if (guess2.isEmpty()) {
            throw new RuntimeException("Guess is empty");
        }

        assertThat(guess2.get().correctPosition()).isEqualTo(0);
        assertThat(guess2.get().correctNumber()).isEqualTo(4);
    }

    @Test
    void testGuessValidationWhenNoCorrectNumber() {
        Optional<Guess> guess = ValidateGuess.validateGuess(10, new GuessDto("5678"));
        if (guess.isEmpty()) {
            throw new RuntimeException("Guess is empty");
        }

        assertThat(guess.get().correctPosition()).isEqualTo(0);
        assertThat(guess.get().correctNumber()).isEqualTo(0);
    }
}
