package database.unittest;

import ch.janishuber.logiclab.adapter.persistence.insert.GameInput;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class createNewGameTest {

    @Test
    void testCreateNewGame() throws IOException {
        int id = GameInput.createNewGame("1234");

        assertThat(id).isGreaterThan(0);
    }
}
