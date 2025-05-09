package ch.janishuber.logiclab.domain.chess.evaluate;

import ch.janishuber.logiclab.domain.chess.board.ChessBoard;
import ch.janishuber.logiclab.domain.chess.board.Field;
import ch.janishuber.logiclab.domain.chess.controller.LegalMovesHandler;
import ch.janishuber.logiclab.domain.chess.enums.FigureColor;
import ch.janishuber.logiclab.domain.chess.evaluate.piecetables.PieceTables;
import ch.janishuber.logiclab.domain.chess.figures.Pawn;

import java.util.List;
import java.util.Optional;


public class evaluateBoard {

    public static int evaluateBoard(ChessBoard chessBoard, FigureColor currentTurn, FigureColor botColor) {
        int pieceValue = getPieceValue(chessBoard, currentTurn);
        int positionValue = getPawnPositionValue(chessBoard, botColor);
        int mobilityValue = getMobilityValue(chessBoard, currentTurn);
        int checkmateValue = checkmateValue(chessBoard, currentTurn, botColor);
        Optional<Boolean> isStalemate = GameStateHelper.getStalemateStatus(chessBoard, currentTurn);

        if (isStalemate.isPresent()) {
            if (isStalemate.get()) {
                return 0;
            } else {
                return checkmateValue;
            }
        }
        return pieceValue + mobilityValue + positionValue;
    }

    private static int getMobilityValue(ChessBoard chessBoard, FigureColor currentTurn) {
        int value = 0;
        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() != null) {
                List<Field> possibleMoves = field.getFigure().getPossibleMoves(chessBoard);
                value += (currentTurn == field.getFigure().figureColor) ? possibleMoves.size() : -possibleMoves.size();
            }
        }
        return value / 2;
    }

    private static int getPieceValue(ChessBoard chessBoard, FigureColor currentTurn) {
        int value = 0;
        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() == null) continue;

            int figureValue = switch (field.getFigure().getClassName()) {
                case "Pawn" -> 100;
                case "Rook" -> 500;
                case "Knight" -> 300;
                case "Bishop" -> 325;
                case "Queen" -> 900;
                case "King" -> 100000;
                default -> 0;
            };
            value += (field.getFigure().figureColor == currentTurn) ? figureValue : -figureValue;
        }
        return value;
    }

    private static int getPawnPositionValue(ChessBoard chessBoard, FigureColor botColor) {
        int value = 0;

        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() instanceof Pawn pawn) {
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

    private static int checkmateValue(ChessBoard chessBoard, FigureColor currentTurn, FigureColor botColor) {
        int counterWhite = 0;
        int counterBlack = 0;

        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() == null) continue;
            LegalMovesHandler lmh = new LegalMovesHandler(chessBoard, field.getFigure().figureColor);
            if (!lmh.getLegalMoves(field.getFigure()).isEmpty()) {
                if (field.getFigure().figureColor == botColor) {
                    counterWhite++;
                } else {
                    counterBlack++;
                }
            }
        }
        if (counterWhite == 0 && counterBlack == 0) {
            return 0;
        } else if (counterWhite == 0) {
            return -1000000;
        } else if (counterBlack == 0) {
            return 1000000;
        }
        return 0;
    }
}
