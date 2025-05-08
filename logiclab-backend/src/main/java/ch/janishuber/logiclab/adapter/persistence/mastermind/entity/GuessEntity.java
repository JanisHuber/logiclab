package ch.janishuber.logiclab.adapter.persistence.mastermind.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "playerguesses")
public class GuessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int guessId;

    private int gameId;

    private String guess;

    private int correctPosition;

    private int correctNumber;

    public GuessEntity() {}

    public GuessEntity(int gameId, String guess, int correctPosition, int correctNumber) {
        this.gameId = gameId;
        this.guess = guess;
        this.correctPosition = correctPosition;
        this.correctNumber = correctNumber;
    }

    public int getGuessId() {
        return guessId;
    }

    public void setGuessId(int guessId) {
        this.guessId = guessId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public int getCorrectPosition() {
        return correctPosition;
    }

    public void setCorrectPosition(int correctPosition) {
        this.correctPosition = correctPosition;
    }

    public int getCorrectNumber() {
        return correctNumber;
    }

    public void setCorrectNumber(int correctNumber) {
        this.correctNumber = correctNumber;
    }
}
