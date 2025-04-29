package org.example.chess.backend.evaluate;

import org.example.chess.backend.board.Field;
import org.example.chess.backend.controller.ChessController;
import org.example.chess.backend.enums.FigureColor;
import org.example.chess.backend.figures.Pawn;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class evaluateBoard {
    public static int evaluateBoard(ChessController controller, Logger logger) {
        int pieceValue = getPieceValue(controller);
        int positionValue = getPawnPositionValue(controller);
        int mobilityValue = getMobilityValue(controller);
        int checkmateValue = checkmateValue(controller);
        Optional<Boolean> isStalemate = controller.getStalemateStatus();

        if (isStalemate.isPresent()) {
            if (isStalemate.get()) {
                logger.info("Stalemate detected");
                return 0;
            } else {
                logger.info("Checkmate detected");
                return checkmateValue;
            }
        }

        return
                pieceValue * 4 +
                mobilityValue * 2 +
                positionValue * 3;
    }

    private static int getMobilityValue(ChessController chessController) {
        int value = 0;
        for (Field field : chessController.chessBoard.getFields()) {
            if (field.figure != null) {
                List<Field> possibleMoves = field.figure.getPossibleMoves();
                value += possibleMoves.size();
            }
        }
        return value / 7;
    }

    private static int getPieceValue(ChessController chessController) {
        int value = 0;
        for (Field field : chessController.chessBoard.getFields()) {
            if (field.figure == null) continue;

            int figureValue = switch (field.figure.getClassName()) {
                case "Pawn" -> 10;
                case "Rook" -> 50;
                case "Knight", "Bishop" -> 30;
                case "Queen" -> 90;
                case "King" -> 10000;
                default -> 0;
            };

            value += (field.figure.figureColor == FigureColor.WHITE) ? figureValue : -figureValue;
        }
        return value;
    }

    private static int getPawnPositionValue(ChessController chessController) {
        int value = 0;
        int[][] centralFields = { {4, 4}, {4, 5}, {5, 4}, {5, 5} };

        for (Field field : chessController.chessBoard.getFields()) {
            if (field.figure instanceof Pawn pawn) {
                int row = convertRowToInt(pawn.position.row);
                int col = pawn.position.column;

                value += (pawn.figureColor == FigureColor.WHITE ? row : (9 - row));

                if (isCentralField(row, col, centralFields)) {
                    value += 20;
                    if (isPawnProtected(field, chessController)) {
                        value += 10;
                    }
                }
            }
        }
        return value / 5;
    }

    private static int convertRowToInt(String row) {
        return row.charAt(0) - 'A' + 1;
    }

    private static boolean isCentralField(int row, int col, int[][] centralFields) {
        for (int[] centralField : centralFields) {
            if (centralField[0] == row && centralField[1] == col) {
                return true;
            }
        }
        return false;
    }

    private static boolean isPawnProtected(Field field, ChessController chessController) {
        for (Field currentField : chessController.chessBoard.getFields()) {
            if (currentField == field) {
                continue;
            }
            if (currentField.figure != null) {
                if (currentField.figure.figureColor == field.figure.figureColor) {
                    List<Field> possibleMoves = currentField.figure.getPossibleMoves();
                    if (possibleMoves.contains(field)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static int checkmateValue(ChessController chessController) {
        int counterWhite = 0;
        int counterBlack = 0;

        for (Field field : chessController.chessBoard.getFields()) {
            if (field.figure != null) {
                if (chessController.getCheckMoveHandler().getCheckedMove(field.figure) == null) {
                    break;
                }
                if (!chessController.getCheckMoveHandler().getCheckedMove(field.figure).isEmpty()) {
                    if (field.figure.figureColor == FigureColor.WHITE) {
                        counterWhite++;
                    } else {
                        counterBlack++;
                    }
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
