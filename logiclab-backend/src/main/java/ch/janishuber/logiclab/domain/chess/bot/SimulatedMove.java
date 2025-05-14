package ch.janishuber.logiclab.domain.chess.bot;

import ch.janishuber.logiclab.domain.chess.board.Field;
import ch.janishuber.logiclab.domain.chess.util.ChessFigure;

public record SimulatedMove(
        Field source,
        Field target,
        ChessFigure capturedFigure,
        boolean wasPromotion,
        ChessFigure promotedTo,
        boolean wasCastling,
        Field rookSource,
        Field rookTarget,
        ChessFigure rookFigure) {
}
