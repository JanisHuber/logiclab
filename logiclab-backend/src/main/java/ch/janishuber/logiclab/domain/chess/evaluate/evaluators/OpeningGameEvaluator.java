package ch.janishuber.logiclab.domain.chess.evaluate.evaluators;

import ch.janishuber.logiclab.domain.chess.board.ChessBoard;
import ch.janishuber.logiclab.domain.chess.board.Field;
import ch.janishuber.logiclab.domain.chess.enums.FigureColor;
import ch.janishuber.logiclab.domain.chess.evaluate.piecetables.PieceTables;

public class OpeningGameEvaluator {

    public static int rateOpeningBoard(ChessBoard chessBoard, FigureColor currentTurn, FigureColor botColor) {
        int overallValue = 0;

        overallValue += getPieceTableValue(chessBoard, botColor);
        overallValue += getMaterialValue(chessBoard, botColor);
        return overallValue;
    }

    private static int getPieceTableValue(ChessBoard chessBoard, FigureColor botColor) {
        int pieceTableValue = 0;
        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() != null) {
                int value = switch (field.getFigure().getClassName()) {
                    case "Pawn" -> PieceTables.getPawnTableValue(field);
                    case "Knight" -> PieceTables.getKnightTableValue(field);
                    case "Bishop" -> PieceTables.getBishopTableValue(field);
                    default -> 0;
                };
                pieceTableValue += (field.getFigure().figureColor == botColor) ? value : -value;
            }
        }
        return pieceTableValue;
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
