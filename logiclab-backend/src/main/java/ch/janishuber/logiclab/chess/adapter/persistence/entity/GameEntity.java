package ch.janishuber.logiclab.chess.adapter.persistence.entity;

import ch.janishuber.logiclab.chess.domain.enums.FigureColor;
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

    private boolean againstAI;

    private String botColor;

    private int botDifficulty;

    public GameEntity() {}

    public GameEntity(String gameState, String boardState, String currentTurn, boolean againstAI, String botColor, int botDifficulty) {
        this.gameState = gameState;
        this.boardState = boardState;
        this.currentTurn = currentTurn;
        this.againstAI = againstAI;
        this.botColor = botColor;
        this.botDifficulty = botDifficulty;
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
    public boolean isAgainstAI() {
        return againstAI;
    }
    public String getBotColor() {
        return botColor;
    }
    public int getBotDifficulty() { return botDifficulty; }

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
    public void setAgainstAI(boolean againstAI) {
        this.againstAI = againstAI;
    }
    public void setBotColor(String botColor) {
        this.botColor = botColor;
    }
    public void setBotDifficulty(int botDifficulty) {}
}
