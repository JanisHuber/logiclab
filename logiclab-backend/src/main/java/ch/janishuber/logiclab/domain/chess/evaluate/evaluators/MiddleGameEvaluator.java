package ch.janishuber.logiclab.domain.chess.evaluate.evaluators;

import ch.janishuber.logiclab.domain.chess.board.ChessBoard;
import ch.janishuber.logiclab.domain.chess.board.Field;
import ch.janishuber.logiclab.domain.chess.enums.FigureColor;

public class MiddleGameEvaluator {

    public static int rateMiddleGameBoard(ChessBoard chessBoard, FigureColor currentTurn, FigureColor botColor) {
        return getMaterialValue(chessBoard, botColor);
    }

    private static int getMaterialValue(ChessBoard chessBoard, FigureColor botColor) {
        int pieceMaterialValue = 0;

        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() != null) {
                int value = switch (field.getFigure().getClassName()) {
                    case "Pawn" -> 100;
                    case "Knight", "Bishop" -> 300;
                    case "Rook" -> 500;
                    case "Queen" -> 900;
                    case "King" -> 1000;
                    default -> 0;
                };
                pieceMaterialValue += ((field.getFigure().figureColor == botColor) ? value : -value);
            }
        }
        return pieceMaterialValue;
    }
}
