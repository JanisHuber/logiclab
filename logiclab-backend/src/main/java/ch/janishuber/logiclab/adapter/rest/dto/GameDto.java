package ch.janishuber.logiclab.adapter.rest.dto;

public class GameDto {
    private int gameId;

    public GameDto() {
    }

    public GameDto(int gameId) {
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}

