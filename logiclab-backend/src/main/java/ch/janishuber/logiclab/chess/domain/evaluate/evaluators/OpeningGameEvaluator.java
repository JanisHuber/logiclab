package ch.janishuber.logiclab.chess.domain.evaluate.evaluators;

import ch.janishuber.logiclab.chess.domain.board.ChessBoard;
import ch.janishuber.logiclab.chess.domain.board.Field;
import ch.janishuber.logiclab.chess.domain.controller.LegalMovesHandler;
import ch.janishuber.logiclab.chess.domain.enums.FigureColor;
import ch.janishuber.logiclab.chess.domain.evaluate.piecetables.PieceTables;

import java.util.List;

public class OpeningGameEvaluator {

    public static int rateOpeningBoard(ChessBoard chessBoard, FigureColor currentTurn, FigureColor botColor) {
        int overallValue = 0;

        overallValue += getPieceTableValue(chessBoard, botColor);
        overallValue += getMaterialValue(chessBoard, botColor);
        overallValue += checkmateValue(chessBoard, currentTurn, botColor);
        overallValue += getMobilityValue(chessBoard, currentTurn, botColor);
        overallValue += figureDefenseValue(chessBoard, botColor);
        System.out.println("Opening Game Evaluation: " + "PieceTableValue: " + getPieceTableValue(chessBoard, botColor) +
                " MaterialValue: " + getMaterialValue(chessBoard, botColor) +
                " MobilityValue: " + getMobilityValue(chessBoard, currentTurn, botColor) +
                " CheckmateValue: " + checkmateValue(chessBoard, currentTurn, botColor) +
                " FigureDefenseValue: " + figureDefenseValue(chessBoard, botColor));
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

    private static int getMobilityValue(ChessBoard chessBoard, FigureColor currentTurn, FigureColor botColor) {
        int mobilityValue = 0;
        LegalMovesHandler legalMovesHandler = new LegalMovesHandler(chessBoard, currentTurn);
        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() != null && field.getFigure().figureColor == currentTurn) {
                List<Field> possibleMoves = legalMovesHandler.getLegalMoves(field.getFigure());
                if (possibleMoves != null && !possibleMoves.isEmpty()) {
                    int mobility = possibleMoves.size();
                    mobilityValue += (field.getFigure().figureColor == botColor) ? mobility : -mobility;
                }
            }
        }
        return mobilityValue;
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
        System.out.println("No CheckMate options available");
        return 0;
    }

    private static int figureDefenseValue(ChessBoard chessBoard, FigureColor botColor) {
        int figureDefenseValue = 0;
        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() != null && field.getFigure().figureColor == botColor) {
                if (isFigureCovered(chessBoard, field)) {
                    figureDefenseValue -= field.getFigure().value;
                } else {
                    figureDefenseValue -= field.getFigure().value;
                }
            }
        }
        return figureDefenseValue;
    }

    private static boolean isFigureCovered(ChessBoard chessBoard, Field field) {
        LegalMovesHandler legalMovesHandler = new LegalMovesHandler(chessBoard, field.getFigure().figureColor);
        boolean isCovered = false;
        for (Field f : chessBoard.getFields()) {
            if (f.getFigure() != null && field.getFigure().figureColor == f.getFigure().figureColor) {
                if (legalMovesHandler.getLegalMoves(f.getFigure()).contains(field)) {
                    isCovered = true;
                }
            }
        }
        return isCovered;
    }
}
