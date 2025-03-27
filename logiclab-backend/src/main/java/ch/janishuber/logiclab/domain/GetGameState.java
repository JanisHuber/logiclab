package ch.janishuber.logiclab.domain;

import ch.janishuber.logiclab.adapter.persistence.select.GameOutput;
import ch.janishuber.logiclab.adapter.rest.dto.GameDto;

import java.util.Optional;

public class GetGameState {

    public GetGameState() {}

    public Optional<GameDto> getGameState(int gameId) {
        return GameOutput.getGameState(gameId);
    }
}
