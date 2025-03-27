package database.unittest;

import ch.janishuber.logiclab.adapter.persistence.insert.GuessInput;
import ch.janishuber.logiclab.domain.Guess;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class InsertGuessTest {
    @Test
    void test() throws IOException {
        boolean hasInserted = GuessInput.inputGuess(1, new Guess("1234", 4, 4));

        assertThat(hasInserted).isTrue();
    }
}
