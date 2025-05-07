package ch.janishuber.logiclab.chess.domain.evaluate.evaluators;

import ch.janishuber.logiclab.chess.domain.board.ChessBoard;
import ch.janishuber.logiclab.chess.domain.board.Field;
import ch.janishuber.logiclab.chess.domain.controller.LegalMovesHandler;
import ch.janishuber.logiclab.chess.domain.enums.FigureColor;

public class EndGameEvaluator {

    public static int rateEndGameBoard(ChessBoard chessBoard, FigureColor currentTurn, FigureColor botColor) {
        int value = 0;
        value += getMaterialValue(chessBoard, botColor);
        return value;
    }

    private static int getMaterialValue(ChessBoard chessBoard, FigureColor botColor) {
        int value = 0;
        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() != null) {
                int figValue = switch (field.getFigure().getClassName()) {
                    case "Pawn" -> 150;
                    case "Knight", "Bishop" -> 300;
                    case "Rook" -> 500;
                    case "Queen" -> 900;
                    case "King" -> 0;
                    default -> 0;
                };
                value += (field.getFigure().figureColor == botColor) ? figValue : -figValue;
            }
        }
        return value;
    }
}
