package ch.janishuber.logiclab.chess.adapter.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "chess")
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gameId;

    private String gameState;

    private String boardState;

    private String currentTurn;

    public GameEntity() {}

    public GameEntity(String gameState, String boardState, String currentTurn) {
        this.gameState = gameState;
        this.boardState = boardState;
        this.currentTurn = currentTurn;
    }

    public int getGameId() {
        return gameId;
    }
    public String getGameState() {
        return gameState;
    }
    public String getBoardState() {
        return boardState;
    }
    public String getCurrentTurn() {
        return currentTurn;
    }


    public void setGameState(String gameState) {
        this.gameState = gameState;
    }
    public void setBoardState(String boardState) {
        this.boardState = boardState;
    }
    public void setCurrentTurn(String currentTurn) {
        this.currentTurn = currentTurn;
    }
    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}
