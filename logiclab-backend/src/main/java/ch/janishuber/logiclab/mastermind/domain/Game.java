package ch.janishuber.logiclab.domain;

import java.util.Optional;
import java.util.Random;

public class Game {

    private static final int GAME_ID_NOT_SET = -1;

    private final int id;

    private final String masterMindNumber;

    private String gameStatus;

    private final Guess[] guesses;

    private Game(int id, String masterMindNumber, String gameStatus, Guess[] guesses) {
        this.id = id;
        this.masterMindNumber = masterMindNumber;
        this.gameStatus = gameStatus;
        this.guesses = guesses;
    }

    public static Game startNewGame() {
        Random rand = new Random();
        String generatedMasterMindNumber = rand.nextInt(1000, 10000) + "";
        if (generatedMasterMindNumber.contains("0")) {
            generatedMasterMindNumber = generatedMasterMindNumber.replace("0", "1");
        }
        Guess[] guesses = new Guess[8];
        return new Game(GAME_ID_NOT_SET, generatedMasterMindNumber, "started", guesses);
    }

    public static Game ofExisting(int id, String masterMindNumber, String gameStatus, Guess[] guesses) {
        return new Game(id, masterMindNumber, gameStatus, guesses);
    }

    public int getId() {
        return id;
    }

    public String getMasterMindNumber() {
        return masterMindNumber;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public Guess[] getAttemptsAmount() {
        return guesses;
    }


    public Optional<Guess> isValidGuess(String guess) {
        int correctPositions = checkCorrectPosition(guess);
        int correctNumbers = checkCorrectNumber(guess);

        if (correctPositions == 4) {
            this.gameStatus = "won";
        }
        if (guesses.length >= 7 && !gameStatus.equals("won")) {
            this.gameStatus = "lost";
        }

        return Optional.of(new Guess(guess, correctPositions, correctNumbers));
    }

    private int checkCorrectPosition(String guess) {
        int correctPositions = 0;

        for (int i = 0; i < 4; i++) {
            if (masterMindNumber.charAt(i) == guess.charAt(i)) {
                correctPositions++;
            }
        }

        return correctPositions;
    }

    private int checkCorrectNumber(String guess) {
        int correctNumbers = 0;
        String tempMasterMindNumber = this.masterMindNumber;

        for (char c : guess.toCharArray()) {
            if (tempMasterMindNumber.contains(String.valueOf(c))) {
                correctNumbers++;
                tempMasterMindNumber = tempMasterMindNumber.replaceFirst(String.valueOf(c), "0");
            }
        }

        return correctNumbers;
    }
}
