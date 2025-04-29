package ch.janishuber.logiclab.chess.adapter.rest.dto;

public record GameDto(int gameId, String gameState, String boardState, String currentTurn) {}
