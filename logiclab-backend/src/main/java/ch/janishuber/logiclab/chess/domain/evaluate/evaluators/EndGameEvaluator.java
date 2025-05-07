package ch.janishuber.logiclab.chess.domain.evaluate.evaluators;

import ch.janishuber.logiclab.chess.domain.board.ChessBoard;
import ch.janishuber.logiclab.chess.domain.board.Field;
import ch.janishuber.logiclab.chess.domain.controller.LegalMovesHandler;
import ch.janishuber.logiclab.chess.domain.enums.FigureColor;

public class EndGameEvaluator {

    public static int rateEndGameBoard(ChessBoard chessBoard, FigureColor currentTurn, FigureColor botColor) {
        int value = 0;
        value += getMaterialValue(chessBoard, botColor);
        value += getKingActivityValue(chessBoard, botColor);
        value += getPassedPawnValue(chessBoard, botColor);
        value += checkmateValue(chessBoard, currentTurn, botColor);
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

    private static int getKingActivityValue(ChessBoard chessBoard, FigureColor botColor) {
        int value = 0;
        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() != null && field.getFigure().getClassName().equals("King")) {
                int row = field.getRow() - 1;
                int col = field.getColumn().charAt(0) - 'A';
                double distToCenter = Math.abs(row - 3.5) + Math.abs(col - 3.5);
                int kingActivity = 20 - (int) (distToCenter * 5);
                value += (field.getFigure().figureColor == botColor) ? kingActivity : -kingActivity;
            }
        }
        return value;
    }

    private static int getPassedPawnValue(ChessBoard chessBoard, FigureColor botColor) {
        int value = 0;
        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() != null && field.getFigure().getClassName().equals("Pawn")) {
                if (isPassedPawn(field, chessBoard)) {
                    value += (field.getFigure().figureColor == botColor) ? 80 : -80;
                }
            }
        }
        return value;
    }

    private static boolean isPassedPawn(Field pawnField, ChessBoard chessBoard) {
        int row = pawnField.getRow();
        int col = pawnField.getColumn().charAt(0) - 'A';
        FigureColor color = pawnField.getFigure().figureColor;
        int dir = (color == FigureColor.WHITE) ? 1 : -1;
        for (int r = row + dir; r >= 1 && r <= 8; r += dir) {
            Field f = chessBoard.getField(String.valueOf((char) ('A' + col)), r);
            if (f.getFigure() != null && f.getFigure().getClassName().equals("Pawn")
                    && f.getFigure().figureColor != color) {
                return false;
            }
        }
        return true;
    }

    private static int checkmateValue(ChessBoard chessBoard, FigureColor currentTurn, FigureColor botColor) {
        int botCounter = 0;
        int opponentCounter = 0;

        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() != null) {
                LegalMovesHandler legalMovesHandler = new LegalMovesHandler(chessBoard, currentTurn);
                if (legalMovesHandler.getLegalMoves(field.getFigure()) == null) {
                    break;
                }
                if (!legalMovesHandler.getLegalMoves(field.getFigure()).isEmpty()) {
                    if (field.getFigure().figureColor == botColor) {
                        botCounter++;
                    } else {
                        opponentCounter++;
                    }
                }
            }
        }
        if (botCounter == 0 && opponentCounter == 0) {
            return 0;
        } else if (botCounter == 0) {
            return -1000000;
        } else if (opponentCounter == 0) {
            return 1000000;
        }
        return 0;
    }
}
