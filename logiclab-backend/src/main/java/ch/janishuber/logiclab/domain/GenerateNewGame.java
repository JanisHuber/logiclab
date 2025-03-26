package ch.janishuber.logiclab.domain;

import ch.janishuber.logiclab.adapter.persistence.insert.GameInput;
import ch.janishuber.logiclab.adapter.rest.dto.GameDto;

import java.io.IOException;
import java.util.Random;

public class GenerateNewGame {
    private final int gameId;

    public GenerateNewGame() throws IOException {
        Random rand = new Random();
        String generatedMasterMindNumber = rand.nextInt(10000) + "";

        this.gameId = GameInput.createNewGame(generatedMasterMindNumber);
    }

    public GameDto getGameDto() {
        return new GameDto(gameId);
    }
}
