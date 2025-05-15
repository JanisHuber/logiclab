package ch.janishuber.logiclab.domain.chess.evaluate.evaluators;

import ch.janishuber.logiclab.domain.chess.board.ChessBoard;
import ch.janishuber.logiclab.domain.chess.board.Field;
import ch.janishuber.logiclab.domain.chess.enums.FigureColor;
import ch.janishuber.logiclab.domain.chess.util.GameStateHelper;
import ch.janishuber.logiclab.domain.chess.figures.Pawn;
import ch.janishuber.logiclab.domain.chess.figures.King;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EndGameEvaluator implements BoardEvaluator {

    private static final int PASSED_PAWN_BONUS = 50;
    private static final int DOUBLE_PAWN_PENALTY = -20;
    private static final int ISOLATED_PAWN_PENALTY = -15;
    private static final int KING_CENTRALIZATION_BONUS = 10;
    private static final int ROOK_SEVENTH_RANK_BONUS = 30;
    private static final String[] COLUMNS = { "A", "B", "C", "D", "E", "F", "G", "H" };

    public int evaluateBoard(ChessBoard chessBoard, FigureColor botColor) {
        int overallValue = 0;
        overallValue += getPieceValue(chessBoard, botColor);
        overallValue += getPawnStructureValue(chessBoard, botColor);
        overallValue += getMobilityValue(chessBoard, botColor) / 2;
        overallValue += getKingPositionValue(chessBoard, botColor);
        overallValue += getRookPositionValue(chessBoard, botColor);
        overallValue += checkmateValue(chessBoard, botColor);
        return overallValue;
    }

    private int getPawnStructureValue(ChessBoard chessBoard, FigureColor botColor) {
        int value = 0;
        value += evaluatePassedPawns(chessBoard, botColor);
        value += evaluateDoublePawns(chessBoard, botColor);
        value += evaluateIsolatedPawns(chessBoard, botColor);
        return value;
    }

    private int evaluatePassedPawns(ChessBoard chessBoard, FigureColor botColor) {
        int value = 0;
        List<Field> pawns = getPawnsOfColor(chessBoard, botColor);

        for (Field pawnField : pawns) {
            if (isPassedPawn(pawnField, chessBoard)) {
                value += PASSED_PAWN_BONUS;
                value += (botColor == FigureColor.WHITE) ? pawnField.getRow() * 10 : (7 - pawnField.getRow()) * 10;
            }
        }
        return value;
    }

    private int getColumnIndex(String column) {
        return column.charAt(0) - 'A';
    }

    private boolean isPassedPawn(Field pawnField, ChessBoard chessBoard) {
        int column = getColumnIndex(pawnField.getColumn());
        int row = pawnField.getRow();
        FigureColor color = pawnField.getFigure().figureColor;

        for (int c = Math.max(0, column - 1); c <= Math.min(7, column + 1); c++) {
            if (color == FigureColor.WHITE) {
                for (int r = row + 1; r < 8; r++) {
                    if (isEnemyPawn(chessBoard.getField(COLUMNS[c], r), color))
                        return false;
                }
            } else {
                for (int r = row - 1; r >= 0; r--) {
                    if (isEnemyPawn(chessBoard.getField(COLUMNS[c], r), color))
                        return false;
                }
            }
        }
        return true;
    }

    private boolean isEnemyPawn(Field field, FigureColor ownColor) {
        return field.getFigure() instanceof Pawn &&
                field.getFigure().figureColor != ownColor;
    }

    private int evaluateDoublePawns(ChessBoard chessBoard, FigureColor botColor) {
        int value = 0;
        for (int column = 0; column < 8; column++) {
            int pawnCount = 0;
            for (int row = 0; row < 8; row++) {
                Field field = chessBoard.getField(COLUMNS[column], row);
                if (field.getFigure() instanceof Pawn &&
                        field.getFigure().figureColor == botColor) {
                    pawnCount++;
                }
            }
            if (pawnCount > 1) {
                value += DOUBLE_PAWN_PENALTY * (pawnCount - 1);
            }
        }
        return value;
    }

    private int evaluateIsolatedPawns(ChessBoard chessBoard, FigureColor botColor) {
        int value = 0;
        for (int column = 0; column < 8; column++) {
            boolean hasPawnInColumn = false;
            boolean hasPawnInAdjacentColumns = false;

            for (int row = 0; row < 8; row++) {
                if (isPawnOfColor(chessBoard.getField(COLUMNS[column], row), botColor)) {
                    hasPawnInColumn = true;
                    break;
                }
            }

            if (hasPawnInColumn) {
                if (column > 0) {
                    for (int row = 0; row < 8; row++) {
                        if (isPawnOfColor(chessBoard.getField(COLUMNS[column - 1], row), botColor)) {
                            hasPawnInAdjacentColumns = true;
                            break;
                        }
                    }
                }
                if (!hasPawnInAdjacentColumns && column < 7) {
                    for (int row = 0; row < 8; row++) {
                        if (isPawnOfColor(chessBoard.getField(COLUMNS[column + 1], row), botColor)) {
                            hasPawnInAdjacentColumns = true;
                            break;
                        }
                    }
                }
                if (!hasPawnInAdjacentColumns) {
                    value += ISOLATED_PAWN_PENALTY;
                }
            }
        }
        return value;
    }

    private boolean isPawnOfColor(Field field, FigureColor color) {
        return field.getFigure() instanceof Pawn &&
                field.getFigure().figureColor == color;
    }

    private List<Field> getPawnsOfColor(ChessBoard chessBoard, FigureColor color) {
        return chessBoard.getFields().stream()
                .filter(f -> f.getFigure() instanceof Pawn &&
                        f.getFigure().figureColor == color)
                .collect(Collectors.toList());
    }

    private int getKingPositionValue(ChessBoard chessBoard, FigureColor botColor) {
        int value = 0;
        Field kingField = findKing(chessBoard, botColor);
        if (kingField != null) {
            int centerDistance = Math.abs(3 - getColumnIndex(kingField.getColumn())) + Math.abs(3 - kingField.getRow());
            value += (4 - centerDistance) * KING_CENTRALIZATION_BONUS;
        }
        return value;
    }

    private Field findKing(ChessBoard chessBoard, FigureColor color) {
        return chessBoard.getFields().stream()
                .filter(f -> f.getFigure() instanceof King &&
                        f.getFigure().figureColor == color)
                .findFirst()
                .orElse(null);
    }

    private int getRookPositionValue(ChessBoard chessBoard, FigureColor botColor) {
        int value = 0;
        int seventhRank = (botColor == FigureColor.WHITE) ? 6 : 1;

        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() != null &&
                    field.getFigure().getClassName().equals("Rook") &&
                    field.getFigure().figureColor == botColor &&
                    field.getRow() == seventhRank) {
                value += ROOK_SEVENTH_RANK_BONUS;
            }
        }
        return value;
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
            if (field.getFigure() == null)
                continue;
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
