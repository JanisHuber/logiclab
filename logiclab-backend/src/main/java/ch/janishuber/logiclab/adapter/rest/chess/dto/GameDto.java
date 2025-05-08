package ch.janishuber.logiclab.adapter.rest.chess.dto;

public record GameDto(int gameId, String gameState, String boardState, String currentTurn, boolean againstAI, String botColor) {}
