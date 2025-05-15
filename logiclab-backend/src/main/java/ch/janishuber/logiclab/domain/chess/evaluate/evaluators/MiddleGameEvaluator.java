package ch.janishuber.logiclab.domain.chess.evaluate.evaluators;

import ch.janishuber.logiclab.domain.chess.board.ChessBoard;
import ch.janishuber.logiclab.domain.chess.board.Field;
import ch.janishuber.logiclab.domain.chess.enums.FigureColor;
import ch.janishuber.logiclab.domain.chess.util.GameStateHelper;
import ch.janishuber.logiclab.domain.chess.evaluate.PieceTables;
import ch.janishuber.logiclab.domain.chess.figures.Pawn;

import java.util.List;
import java.util.Optional;

public class MiddleGameEvaluator implements BoardEvaluator {

    public int evaluateBoard(ChessBoard chessBoard, FigureColor botColor) {
        int overallValue = 0;
        overallValue += getPieceValue(chessBoard, botColor);
        overallValue += getPawnPositionValue(chessBoard, botColor) / 2;
        overallValue += getMobilityValue(chessBoard, botColor) / 2;
        overallValue += checkmateValue(chessBoard, botColor);
        overallValue += getKingPositionValue(chessBoard, botColor) / 2;
        return overallValue;
    }

    private static int getMobilityValue(ChessBoard chessBoard, FigureColor botColor) {
        int value = 0;
        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() != null) {
                List<Field> possibleMoves = field.getFigure().getPossibleMoves(chessBoard);
                value += (botColor == field.getFigure().figureColor) ? possibleMoves.size() : -possibleMoves.size();
            }
        }
        return value / 2;
    }

    private static int getPieceValue(ChessBoard chessBoard, FigureColor botColor) {
        int value = 0;
        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() == null) continue;
            int figureValue = switch (field.getFigure().getClassName()) {
                case "Pawn" -> 100;
                case "Rook" -> 500;
                case "Knight" -> 300;
                case "Bishop" -> 325;
                case "Queen" -> 900;
                default -> 0;
            };
            if (field.getFigure().figureColor == botColor) {
                value += figureValue;
            } else {
                value -= figureValue;
            }
        }
        return value;
    }

    private static int getPawnPositionValue(ChessBoard chessBoard, FigureColor botColor) {
        int value = 0;

        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() instanceof Pawn) {
                int tempValue = (botColor == field.getFigure().figureColor) ? PieceTables.getPawnTableValue(field) : -PieceTables.getPawnTableValue(field);
                if (isPawnProtected(field, chessBoard)) {
                    tempValue += 10;
                }
                value += tempValue;
            }
        }
        return value / 2;
    }

    private static boolean isPawnProtected(Field field, ChessBoard chessBoard) {
        for (Field currentField : chessBoard.getFields()) {
            if (currentField == field) {
                continue;
            }
            if (currentField.getFigure() != null) {
                if (currentField.getFigure().figureColor == field.getFigure().figureColor) {
                    List<Field> possibleMoves = currentField.getFigure().getPossibleMoves(chessBoard);
                    if (possibleMoves.contains(field)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static int getKingPositionValue(ChessBoard chessBoard, FigureColor botColor) {
        int value = 0;
        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() != null && field.getFigure().getClassName().equals("King")) {
                int tempValue = (botColor == field.getFigure().figureColor) ? PieceTables.getKingMidGameTableValue(field) : -PieceTables.getKingMidGameTableValue(field);
                value += tempValue;
            }
        }
        return value;
    }

    private static int checkmateValue(ChessBoard chessBoard, FigureColor botColor) {
        Optional<Boolean> gameStateWhite = GameStateHelper.getStalemateStatus(chessBoard, FigureColor.WHITE);
        Optional<Boolean> gameStateBlack = GameStateHelper.getStalemateStatus(chessBoard, FigureColor.BLACK);

        if (gameStateWhite.isPresent()) {
            if (isCheckMate(gameStateWhite)) {
                return (botColor == FigureColor.WHITE) ? -100000 : 100000;
            }
        }
        if (gameStateBlack.isPresent()) {
            if (isCheckMate(gameStateBlack)) {
                return (botColor == FigureColor.BLACK) ? -100000 : 100000;
            }
        }
        return 0;
    }

    private static boolean isCheckMate(Optional<Boolean> gameState) {
        if (gameState.isPresent()) {
            if (!gameState.get()) {
                return true;
            }
        }
        return false;
    }
}
