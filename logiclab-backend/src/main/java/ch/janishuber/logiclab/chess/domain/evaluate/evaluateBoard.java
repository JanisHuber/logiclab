package ch.janishuber.logiclab.chess.domain.evaluate;

import ch.janishuber.logiclab.chess.domain.board.ChessBoard;
import ch.janishuber.logiclab.chess.domain.board.Field;
import ch.janishuber.logiclab.chess.domain.controller.CheckMoveHandler;
import ch.janishuber.logiclab.chess.domain.enums.FigureColor;
import ch.janishuber.logiclab.chess.domain.evaluate.piecetables.PieceTables;
import ch.janishuber.logiclab.chess.domain.figures.Pawn;

import java.util.List;
import java.util.Optional;

public class evaluateBoard {

    public static int evaluateBoard(ChessBoard chessBoard, FigureColor currentTurn, FigureColor botColor) {
        int pieceValue = getPieceValue(chessBoard, currentTurn);
        int pawnPositionValue = getPawnPositionValue(chessBoard, botColor);
        int mobilityValue = getMobilityValue(chessBoard, botColor);
        int checkmateValue = checkmateValue(chessBoard, botColor);
        Optional<Boolean> isStalemate = GameStateHelper.getStalemateStatus(chessBoard, currentTurn);
        int pieceTableValue = getPieceTableValue(chessBoard, botColor);

        if (isStalemate.isPresent()) {
            if (isStalemate.get()) {
                return 0;
            } else {
                return checkmateValue;
            }
        }
        return (pieceValue * 4) +
                (mobilityValue * 2) +
                (pawnPositionValue * 3) +
                (pieceTableValue / 10);
    }

    private static int getPieceTableValue(ChessBoard chessBoard, FigureColor botColor) {
        int value = 0;
        for (Field field : chessBoard.getFields()) {
            if (field.figure == null)
                continue;
            int figureValue = switch (field.figure.getClassName()) {
                case "Pawn" -> PieceTables.getPawnTableValue(field.row, field.column, field.figure.figureColor);
                case "Rook" -> PieceTables.getRookTableValue(field.row, field.column, field.figure.figureColor);
                case "Knight" -> PieceTables.getKnightTableValue(field.row, field.column, field.figure.figureColor);
                case "Bishop" -> PieceTables.getBishopTableValue(field.row, field.column, field.figure.figureColor);
                case "Queen" -> PieceTables.getQueenTableValue(field.row, field.column, field.figure.figureColor);
                case "King" -> PieceTables.getKingTableValue(field.row, field.column, field.figure.figureColor);
                default -> 0;
            };
            value += (field.figure.figureColor == botColor) ? figureValue : -figureValue;
            System.out.println("Value for " + field.figure.getClassName() + " on " + field.row + field.column + ": " + figureValue);
        }
        return value;
    }

    private static int getMobilityValue(ChessBoard chessBoard, FigureColor botColor) {
        int value = 0;
        for (Field field : chessBoard.getFields()) {
            if (field.figure == null)
                continue;

            List<Field> possibleMoves = field.figure.getPossibleMoves(chessBoard);
            if (field.figure.figureColor == botColor) {
                value += possibleMoves.size();
            } else {
                value -= possibleMoves.size();
            }
        }
        return value;
    }

    private static int getPieceValue(ChessBoard chessBoard, FigureColor botColor) {
        int value = 0;
        for (Field field : chessBoard.getFields()) {
            if (field.figure == null)
                continue;

            int figureValue = switch (field.figure.getClassName()) {
                case "Pawn" -> 10;
                case "Rook" -> 50;
                case "Knight", "Bishop" -> 30;
                case "Queen" -> 90;
                case "King" -> 10000;
                default -> 0;
            };

            value += (field.figure.figureColor == botColor) ? figureValue : -figureValue;
        }
        return value;
    }

    private static int getPawnPositionValue(ChessBoard chessBoard, FigureColor botColor) {
        int value = 0;
        int[][] centralFields = { { 4, 4 }, { 4, 5 }, { 5, 4 }, { 5, 5 } };

        for (Field field : chessBoard.getFields()) {
            if (field.figure instanceof Pawn pawn) {
                int row = convertRowToInt(pawn.position.row);
                int col = pawn.position.column;

                value += (pawn.figureColor == botColor ? row : (9 - row));

                if (isCentralField(row, col, centralFields)) {
                    value += 20;
                    if (isPawnProtected(field, chessBoard)) {
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

    private static boolean isPawnProtected(Field field, ChessBoard chessBoard) {
        for (Field currentField : chessBoard.getFields()) {
            if (currentField == field) {
                continue;
            }
            if (currentField.figure != null) {
                if (currentField.figure.figureColor == field.figure.figureColor) {
                    List<Field> possibleMoves = currentField.figure.getPossibleMoves(chessBoard);
                    if (possibleMoves.contains(field)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static int checkmateValue(ChessBoard chessBoard, FigureColor botColor) {
        int botCounter = 0;
        int opponentCounter = 0;

        for (Field field : chessBoard.getFields()) {
            if (field.figure != null) {
                FigureColor currentTurn = field.figure.figureColor;
                CheckMoveHandler checkMoveHandler = new CheckMoveHandler(chessBoard, currentTurn);
                if (checkMoveHandler.getCheckedMove(field.figure) == null) {
                    break;
                }
                if (!checkMoveHandler.getCheckedMove(field.figure).isEmpty()) {
                    if (field.figure.figureColor == botColor) {
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
