package ch.janishuber.logiclab.chess.domain.bot;

import ch.janishuber.logiclab.chess.domain.board.Field;
import ch.janishuber.logiclab.chess.domain.util.ChessFigure;

public record SimulatedMove(Field source, Field target, ChessFigure figure) {}
