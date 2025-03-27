package ch.janishuber.logiclab.domain;

import ch.janishuber.logiclab.adapter.persistence.insert.GameInput;
import ch.janishuber.logiclab.adapter.persistence.insert.GuessInput;
import ch.janishuber.logiclab.adapter.persistence.select.GameOutput;
import ch.janishuber.logiclab.adapter.rest.dto.GuessDto;

import java.util.Optional;


public class ValidateGuess {

    public static Optional<Guess> validateGuess(int gameId, GuessDto guessDto) {
        Optional<String> mastermindNumber = GameOutput.getMastermindNumber(gameId);

        if (mastermindNumber.isEmpty()) {
            return Optional.empty();
        }
        int correctPositions = checkCorrectPosition(mastermindNumber.get(), guessDto.guess());
        int correctNumbers = checkCorrectNumber(mastermindNumber.get(), guessDto.guess());

        Guess guess = new Guess(guessDto.guess(), correctPositions, correctNumbers);
        GuessInput.inputGuess(gameId, guess);
        if (guess.correctPosition() == 4) {
            GameInput.updateGameStatus(gameId, "won");
        }
        return Optional.of(guess);
    }

    private static int checkCorrectPosition(String mastermindNumber, String guess) {
        int correctPositions = 0;

        for (int i = 0; i < 4; i++) {
            if (mastermindNumber.charAt(i) == guess.charAt(i)) {
                correctPositions++;
            }
        }

        return correctPositions;
    }

    private static int checkCorrectNumber(String mastermindNumber, String guess) {
        int correctNumbers = 0;

        for (char c : guess.toCharArray()) {
            if (mastermindNumber.contains(String.valueOf(c))) {
                correctNumbers++;
                mastermindNumber = mastermindNumber.replaceFirst(String.valueOf(c), "0");
            }
        }

        return correctNumbers;
    }
}
