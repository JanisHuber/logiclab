package ch.janishuber.logiclab.mastermind.domain;

public record Guess(String guess, int correctPositions, int correctNumbers) {}
