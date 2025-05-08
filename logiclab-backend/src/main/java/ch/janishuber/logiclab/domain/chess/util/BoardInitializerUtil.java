package ch.janishuber.logiclab.domain.chess.util;

import ch.janishuber.logiclab.domain.chess.board.ChessBoard;
import ch.janishuber.logiclab.domain.chess.board.Field;
import ch.janishuber.logiclab.domain.chess.enums.FigureColor;
import ch.janishuber.logiclab.domain.chess.figures.Rook;
import ch.janishuber.logiclab.domain.chess.figures.Pawn;
import ch.janishuber.logiclab.domain.chess.figures.Knight;
import ch.janishuber.logiclab.domain.chess.figures.Bishop;
import ch.janishuber.logiclab.domain.chess.figures.Queen;
import ch.janishuber.logiclab.domain.chess.figures.King;

import java.util.ArrayList;
import java.util.List;

public class BoardInitializerUtil {

    public static ChessBoard Initialize(ChessBoard chessBoard) {
        List<Field> fields = new ArrayList<>();

        for (int colIndex = 1; colIndex <= 8; colIndex++) {
            for (int row = 1; row <= 8; row++) {
                String column = Character.toString((char) ('A' + colIndex - 1));
                setUpFields(row, column, fields);
            }
        }

        chessBoard.setFields(fields);
        return chessBoard;
    }

    private static void setUpFields(int row, String column, List<Field> fields) {
        Field f = new Field(column, row);

        if (isRookPosition(row, column)) addRook(f, row);
        else if (isPawnPosition(row)) addPawn(f, row);
        else if (isKnightPosition(row, column)) addKnight(f, row);
        else if (isBishopPosition(row, column)) addBishop(f, row);
        else if (isQueenPosition(row, column)) addQueen(f, row);
        else if (isKingPosition(row, column)) addKing(f, row);

        fields.add(f);
    }

    private static void addPawn(Field f, int row) {
        f.setFigure(new Pawn());
        f.getFigure().position = f;
        f.getFigure().figureColor = (row == 2) ? FigureColor.WHITE : FigureColor.BLACK;
    }

    private static void addRook(Field f, int row) {
        f.setFigure(new Rook());
        f.getFigure().position = f;
        f.getFigure().figureColor = (row == 1) ? FigureColor.WHITE : FigureColor.BLACK;
    }

    private static void addKnight(Field f, int row) {
        f.setFigure(new Knight());
        f.getFigure().position = f;
        f.getFigure().figureColor = (row == 1) ? FigureColor.WHITE : FigureColor.BLACK;
    }

    private static void addBishop(Field f, int row) {
        f.setFigure(new Bishop());
        f.getFigure().position = f;
        f.getFigure().figureColor = (row == 1) ? FigureColor.WHITE : FigureColor.BLACK;
    }

    private static void addQueen(Field f, int row) {
        f.setFigure(new Queen());
        f.getFigure().position = f;
        f.getFigure().figureColor = (row == 1) ? FigureColor.WHITE : FigureColor.BLACK;
    }

    private static void addKing(Field f, int row) {
        f.setFigure(new King());
        f.getFigure().position = f;
        f.getFigure().figureColor = (row == 1) ? FigureColor.WHITE : FigureColor.BLACK;
    }

    private static boolean isRookPosition(int row, String column) {
        return (row == 1 || row == 8) && (column.equals("A") || column.equals("H"));
    }

    private static boolean isPawnPosition(int row) {
        return row == 2 || row == 7;
    }

    private static boolean isKnightPosition(int row, String column) {
        return (row == 1 || row == 8) && (column.equals("B") || column.equals("G"));
    }

    private static boolean isBishopPosition(int row, String column) {
        return (row == 1 || row == 8) && (column.equals("C") || column.equals("F"));
    }

    private static boolean isQueenPosition(int row, String column) {
        return (row == 1 && column.equals("D")) || (row == 8 && column.equals("D"));
    }

    private static boolean isKingPosition(int row, String column) {
        return (row == 1 && column.equals("E")) || (row == 8 && column.equals("E"));
    }
}
