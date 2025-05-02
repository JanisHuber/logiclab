package ch.janishuber.logiclab.chess.domain.evaluate;

import ch.janishuber.logiclab.chess.domain.board.ChessBoard;
import ch.janishuber.logiclab.chess.domain.board.Field;
import ch.janishuber.logiclab.chess.domain.controller.CheckMoveHandler;
import ch.janishuber.logiclab.chess.domain.enums.FigureColor;
import ch.janishuber.logiclab.chess.domain.evaluate.evaluators.EndGameEvaluator;
import ch.janishuber.logiclab.chess.domain.evaluate.evaluators.MiddleGameEvaluator;
import ch.janishuber.logiclab.chess.domain.evaluate.evaluators.OpeningGameEvaluator;
import ch.janishuber.logiclab.chess.domain.evaluate.piecetables.PieceTables;


public class evaluateBoard {

    public static int evaluateBoard(ChessBoard chessBoard, FigureColor currentTurn, FigureColor botColor) {
        GamePhase gamePhase = PhaseEvaluator.evaluatePhase(chessBoard);

        if (gamePhase == GamePhase.OPENING) {
            return OpeningGameEvaluator.rateOpeningBoard(chessBoard, currentTurn, botColor);
        } else if (gamePhase == GamePhase.MIDDLEGAME) {
            return MiddleGameEvaluator.rateMiddleGameBoard(chessBoard, currentTurn, botColor);
        } else {
            return EndGameEvaluator.rateEndGameBoard(chessBoard, currentTurn, botColor);
        }
        return 0;
    }


}
