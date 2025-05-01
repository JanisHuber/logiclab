package ch.janishuber.logiclab.chess.domain.evaluate.piecetables;

import ch.janishuber.logiclab.chess.domain.board.Field;
import ch.janishuber.logiclab.chess.domain.enums.FigureColor;

public class PieceTables {
    public static int[][] pawnTable = {
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 50, 50, 50, 50, 50, 50, 50, 50 },
            { 10, 10, 20, 30, 30, 20, 10, 10 },
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

    public static int[][] rookTable = {
            { 0, 0, 5, 10, 10, 5, 0, 0 },
            { -5, 0, 0, 0, 0, 0, 0, -5 },
            { -5, 0, 0, 0, 0, 0, 0, -5 },
            { -5, 0, 0, 0, 0, 0, 0, -5 },
            { -5, 0, 0, 0, 0, 0, 0, -5 },
            { -5, 0, 0, 0, 0, 0, 0, -5 },
            { -5, 10, 10, 10, 10, 10, 10, -5 },
            { 0, 0, 5, 10, 10, 5, 0, 0 }
    };

    public static int getPawnTableValue(Field field) {
        int row = getTableRow(field);
        int col = getTableCol(field);
        return pawnTable[row][col];
    }

    /*
     * public static int getKnightTableValue(String row, int col, FigureColor
     * figureColor) {
     * rowInt = (figureColor == FigureColor.WHITE) ? rowInt : 7 - rowInt;
     * return knightTable[rowInt][col - 1];
     * }
     * 
     * public static int getBishopTableValue(String row, int col, FigureColor
     * figureColor) {
     * rowInt = (figureColor == FigureColor.WHITE) ? rowInt : 7 - rowInt;
     * return bishopTable[rowInt][col - 1];
     * }
     * 
     * public static int getRookTableValue(String row, int col, FigureColor
     * figureColor) {
     * rowInt = (figureColor == FigureColor.WHITE) ? rowInt : 7 - rowInt;
     * return rookTable[rowInt][col - 1];
     * }
     */

    private static int getTableRow(Field field) {
        int rowIndex = field.getRow() - 1;
        return (field.getFigure().figureColor == FigureColor.BLACK) ? rowIndex : 7 - rowIndex;
    }

    private static int getTableCol(Field field) {
        return field.getColumn().charAt(0) - 'A';
    }

}
