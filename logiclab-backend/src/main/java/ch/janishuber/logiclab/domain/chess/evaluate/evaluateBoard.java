package ch.janishuber.logiclab.domain.chess.evaluate;

import ch.janishuber.logiclab.domain.chess.board.ChessBoard;
import ch.janishuber.logiclab.domain.chess.enums.FigureColor;
import ch.janishuber.logiclab.domain.chess.evaluate.evaluators.BoardEvaluator;
import ch.janishuber.logiclab.domain.chess.util.GamePhase;

public class evaluateBoard {

    public static int evaluateBoard(ChessBoard chessBoard , FigureColor botColor) {
        GamePhase gamePhase = PhaseEvaluator.evaluatePhase(chessBoard);
        BoardEvaluator boardEvaluator = BoardEvaluator.of(gamePhase);

        return boardEvaluator.evaluateBoard(chessBoard, botColor);
    }
}
