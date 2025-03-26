package database.acceptancetest;

import ch.janishuber.logiclab.adapter.persistence.insert.GameInput;
import ch.janishuber.logiclab.adapter.persistence.select.GameOutput;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateGameStatusTest {

    @Test
    void test() throws IOException {
        int gameId = GameInput.createNewGame("1234");


        boolean hasUpdated = GameInput.updateGameStatus(gameId, "won");
        assertThat(hasUpdated).isTrue();


        String gameStatus = GameOutput.getGameStatus(gameId);
        assertThat(gameStatus).isEqualTo("won");
    }
}
