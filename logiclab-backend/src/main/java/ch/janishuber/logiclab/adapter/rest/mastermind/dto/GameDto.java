package ch.janishuber.logiclab.adapter.rest.mastermind.dto;

import ch.janishuber.logiclab.domain.mastermind.Guess;

public record GameDto(int gameId, String gameState, Guess[] guesses) {}

