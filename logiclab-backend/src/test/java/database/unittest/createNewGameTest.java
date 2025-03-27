package database.unittest;

import ch.janishuber.logiclab.adapter.persistence.insert.GameInput;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class createNewGameTest {

    @Test
    void testCreateNewGame() throws IOException {
        Optional<Integer> id = GameInput.createNewGame("1234");
        if (id.isEmpty()) {
            throw new RuntimeException("Game id is empty");
        }

        assertThat(id.get()).isGreaterThan(0);
    }
}
