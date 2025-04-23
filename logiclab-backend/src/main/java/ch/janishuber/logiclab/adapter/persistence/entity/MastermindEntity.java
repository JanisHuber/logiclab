package ch.janishuber.logiclab.adapter.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "mastermind")
public class MastermindEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gameId;

    private String mastermindNumber;

    private String gameStatus;

    public MastermindEntity() {}

    public MastermindEntity(String mastermindNumber, String gameStatus) {
        this.mastermindNumber = mastermindNumber;
        this.gameStatus = gameStatus;
    }

    public int getGameId() {
        return gameId;
    }

    public String getMastermindNumber() {
        return mastermindNumber;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }
}
