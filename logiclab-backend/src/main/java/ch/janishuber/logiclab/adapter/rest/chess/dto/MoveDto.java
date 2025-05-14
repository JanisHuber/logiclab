package ch.janishuber.logiclab.adapter.rest.chess.dto;

public record MoveDto(String source, String target, String promotingFigureClassName) {}
