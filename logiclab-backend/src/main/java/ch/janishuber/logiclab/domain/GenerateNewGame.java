package ch.janishuber.logiclab.domain;

import ch.janishuber.logiclab.adapter.persistence.insert.GameInput;
import ch.janishuber.logiclab.adapter.persistence.select.GameOutput;
import ch.janishuber.logiclab.adapter.rest.dto.GameDto;

import java.util.Optional;
import java.util.Random;

public class GenerateNewGame {
    private final int gameId;

    public GenerateNewGame() {
        Random rand = new Random();
        String generatedMasterMindNumber = rand.nextInt(10000) + "";

        Optional<Integer> gameId = GameInput.createNewGame(generatedMasterMindNumber);

        if (gameId.isEmpty()) {
            throw new IllegalStateException("Couldn't create new game");
        }
        this.gameId = gameId.get();
    }

    public Optional<GameDto> getGameDto() {
        return GameOutput.getGameState(this.gameId);
    }
}
