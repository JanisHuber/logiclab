package ch.janishuber.logiclab.mastermind.adapter.rest.dto;

import ch.janishuber.logiclab.mastermind.domain.Guess;

public record GameDto(int gameId, String gameState, Guess[] guesses) {}

