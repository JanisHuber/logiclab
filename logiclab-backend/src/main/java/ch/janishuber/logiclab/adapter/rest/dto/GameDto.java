package ch.janishuber.logiclab.adapter.rest.dto;

import ch.janishuber.logiclab.domain.Guess;

public record GameDto(int gameId, String gameState, Guess[] guesses) {}

