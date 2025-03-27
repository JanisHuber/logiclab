package database.acceptancetest;

import ch.janishuber.logiclab.adapter.persistence.insert.GameInput;
import ch.janishuber.logiclab.adapter.persistence.select.GameOutput;
import ch.janishuber.logiclab.adapter.rest.dto.GameDto;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateGameStatusTest {

    @Test
    void test() throws IOException {
        Optional<Integer> gameId = GameInput.createNewGame("1234");
        if (gameId.isEmpty()) {
            throw new RuntimeException("GameId is empty");
        }

        boolean hasUpdated = GameInput.updateGameStatus(gameId.get(), "won");
        assertThat(hasUpdated).isTrue();


        Optional<GameDto> gameDto = GameOutput.getGameState(gameId.get());
        if (gameDto.isEmpty()) {
            throw new RuntimeException("GameStatus is empty");
        }

        assertThat(gameDto.get().gameState()).isEqualTo("won");
    }
}
