package ch.janishuber.logiclab.chess.domain.evaluate.evaluators;

import ch.janishuber.logiclab.chess.domain.board.ChessBoard;
import ch.janishuber.logiclab.chess.domain.board.Field;
import ch.janishuber.logiclab.chess.domain.controller.CheckMoveHandler;
import ch.janishuber.logiclab.chess.domain.enums.FigureColor;
import ch.janishuber.logiclab.chess.domain.evaluate.piecetables.PieceTables;

public class MiddleGameEvaluator {

    public static int rateMiddleGameBoard(ChessBoard chessBoard, FigureColor currentTurn, FigureColor botColor) {
        int overallValue = 0;

        overallValue += getPieceTableValue(chessBoard, currentTurn);
        overallValue += getMaterialValue(chessBoard, currentTurn);
        overallValue += checkmateValue(chessBoard, currentTurn, botColor);

        return overallValue;
    }

    private static int getPieceTableValue(ChessBoard chessBoard, FigureColor currentTurn) {
        int pieceTableValue = 0;
        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() != null) {
                int value = switch (field.getFigure().getClassName()) {
                    case "Pawn" -> PieceTables.getPawnTableValue(field);
                    case "Knight" -> PieceTables.getKnightTableValue(field);
                    case "Bishop" -> PieceTables.getBishopTableValue(field);
                    case "Rook" -> PieceTables.getRookTableValue(field);
                    default -> 0;
                };
                pieceTableValue += (field.getFigure().figureColor == currentTurn) ? value : -value;
            }
        }
        return pieceTableValue / 10;
    }

    private static int getMaterialValue(ChessBoard chessBoard, FigureColor currentTurn) {
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
                pieceMaterialValue += ((field.getFigure().figureColor == currentTurn) ? value : -value);
            }
        }
        return pieceMaterialValue;
    }

    private static int checkmateValue(ChessBoard chessBoard, FigureColor currentTurn, FigureColor botColor) {
        int botCounter = 0;
        int opponentCounter = 0;

        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() != null) {
                CheckMoveHandler checkMoveHandler = new CheckMoveHandler(chessBoard, currentTurn);
                if (checkMoveHandler.getCheckedMove(field.getFigure()) == null) {
                    break;
                }
                if (!checkMoveHandler.getCheckedMove(field.getFigure()).isEmpty()) {
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
        System.out.println("No CheckMate options available");
        return 0;
    }
}
