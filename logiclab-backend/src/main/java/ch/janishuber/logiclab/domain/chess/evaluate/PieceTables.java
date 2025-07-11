package ch.janishuber.logiclab.domain.chess.evaluate;

import ch.janishuber.logiclab.domain.chess.board.Field;
import ch.janishuber.logiclab.domain.chess.enums.FigureColor;

public class PieceTables {
    public static int[][] pawnTable = {
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 40, 40, 40, 40, 40, 40, 40, 40 },
            { 10, 10, 10, 10, 10, 10, 10, 10 },
            { 5, 5, 10, 25, 25, 10, 5, 5 },
            { 0, 0, 0, 20, 20, 0, 0, 0 },
            { 5, -5, -10, 0, 0, -10, -5, 5 },
            { 5, 10, 10, -20, -20, 10, 10, 5 },
            { 0, 0, 0, 0, 0, 0, 0, 0 }
    };

    public static int[][] knightTable = {
            { -50, -40, -30, -30, -30, -30, -40, -50 },
            { -40, -20, 0, 5, 5, 0, -20, -40 },
            { -30, 5, 10, 15, 15, 10, 5, -30 },
            { -30, 0, 15, 20, 20, 15, 0, -30 },
            { -30, 5, 15, 20, 20, 15, 5, -30 },
            { -30, 0, 10, 15, 15, 10, 0, -30 },
            { -40, -20, 0, 0, 0, 0, -20, -40 },
            { -50, -40, -30, -30, -30, -30, -40, -50 }
    };

    public static int[][] bishopTable = {
            { -20, -10, -10, -10, -10, -10, -10, -20 },
            { -10, 5, 0, 0, 0, 0, 5, -10 },
            { -10, 10, 10, 10, 10, 10, 10, -10 },
            { -10, 0, 10, 10, 10, 10, 0, -10 },
            { -10, 5, 5, 10, 10, 5, 5, -10 },
            { -10, 0, 5, 10, 10, 5, 0, -10 },
            { -10, 0, 0, 0, 0, 0, 0, -10 },
            { -20, -10, -10, -10, -10, -10, -10, -20 }
    };

    public static int[][] kingMidGameTable = {
            { -30, -40, -40, -50, -50, -40, -40, -30 },
            { -30, -40, -40, -50, -50, -40, -40, -30 },
            { -30, -40, -40, -50, -50, -40, -40, -30 },
            { -30, -40, -40, -50, -50, -40, -40, -30 },
            { 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 },
            { 10 , 10 , 10 , 10 , 10 , 10 , 10 , 10 },
            { 10 , 10 , 10 , 10 , 10 , 10 , 10 , 10 },
            { 0 , 30 , 20 , 0 , 0 , 10 , 30 , 0 }
    };

    public static int getPawnTableValue(Field field) {
        int row = getTableRow(field);
        int col = getTableCol(field);
        return pawnTable[col][row];
    }

    public static int getKnightTableValue(Field field) {
        int row = getTableRow(field);
        int col = getTableCol(field);
        return knightTable[col][row];
    }

    public static int getBishopTableValue(Field field) {
        int row = getTableRow(field);
        int col = getTableCol(field);
        return bishopTable[col][row];
    }

    public static int getKingMidGameTableValue(Field field) {
        int row = getTableRow(field);
        int col = getTableCol(field);
        return kingMidGameTable[col][row];
    }

    private static int getTableRow(Field field) {
        int rowIndex = field.getRow() - 1;
        return (field.getFigure().figureColor == FigureColor.BLACK) ? rowIndex : 7 - rowIndex;
    }

    private static int getTableCol(Field field) {
        return field.getColumn().charAt(0) - 'A';
    }

}
