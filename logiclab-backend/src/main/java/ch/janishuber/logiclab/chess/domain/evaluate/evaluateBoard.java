package ch.janishuber.logiclab.chess.domain.evaluate;

import ch.janishuber.logiclab.chess.domain.board.ChessBoard;
import ch.janishuber.logiclab.chess.domain.board.Field;
import ch.janishuber.logiclab.chess.domain.enums.FigureColor;
import ch.janishuber.logiclab.chess.domain.evaluate.piecetables.PieceTables;

public class evaluateBoard {

    public static int evaluateBoard(ChessBoard chessBoard, FigureColor currentTurn, FigureColor botColor) {
        GamePhase gamePhase = PhaseEvaluator.evaluatePhase(chessBoard);

        if (gamePhase == GamePhase.OPENING) {
            return rateOpeningBoard(chessBoard, currentTurn, botColor);
        } else if (gamePhase == GamePhase.MIDDLEGAME) {
            // return rateMiddleGameBoard(chessBoard, currentTurn, botColor);
        } else {
            // return rateEndGameBoard(chessBoard, currentTurn, botColor);
        }
        return 0;
    }

    private static int rateOpeningBoard(ChessBoard chessBoard, FigureColor currentTurn, FigureColor botColor) {
        int overallValue = 0;

        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() != null) {
                int value = switch (field.getFigure().getClassName()) {
                    case "Pawn" -> PieceTables.getPawnTableValue(field);
                    default -> 0;
                };
                overallValue += (field.getFigure().figureColor == currentTurn) ? value : -value;
            }
        }

        return overallValue;
    }
}
