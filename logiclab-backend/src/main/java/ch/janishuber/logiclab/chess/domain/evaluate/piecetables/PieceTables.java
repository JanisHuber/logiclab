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
            { -1000, -5, -10, 0, 0, -10, -5, 5 },
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
        System.out.println("For Field: " + field.row + field.column);
        int row = getTableRow(field, field.figure.figureColor);
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

    public static int getTableRow(Field field, FigureColor color) {
        int rowIndex = 8 - field.column;
        System.out.println((color == FigureColor.WHITE) ? rowIndex : 7 - rowIndex);
        return (color == FigureColor.WHITE) ? rowIndex : 7 - rowIndex;
    }

    public static int getTableCol(Field field) {
        System.out.println(field.row.charAt(0) - 'A');
        return field.row.charAt(0) - 'A';
    }

}
