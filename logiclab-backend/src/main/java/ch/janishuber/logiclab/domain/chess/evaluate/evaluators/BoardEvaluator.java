package ch.janishuber.logiclab.domain.chess.evaluate.evaluators;

import ch.janishuber.logiclab.domain.chess.board.ChessBoard;
import ch.janishuber.logiclab.domain.chess.enums.FigureColor;
import ch.janishuber.logiclab.domain.chess.util.GamePhase;

public interface BoardEvaluator {
    int evaluateBoard(ChessBoard chessBoard, FigureColor botColor);

    static BoardEvaluator of(GamePhase gamePhase) {
        return switch (gamePhase) {
            case OPENING -> new OpeningGameEvaluator();
            case MIDDLEGAME -> new MiddleGameEvaluator();
            case ENDGAME -> new EndGameEvaluator();
        };
    }
}
