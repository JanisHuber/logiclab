package ch.janishuber.logiclab.chess.domain.util;

import ch.janishuber.logiclab.chess.domain.board.ChessBoard;
import ch.janishuber.logiclab.chess.domain.board.Field;
import ch.janishuber.logiclab.chess.domain.enums.FigureColor;
import ch.janishuber.logiclab.chess.domain.figures.Rook;
import ch.janishuber.logiclab.chess.domain.figures.Pawn;
import ch.janishuber.logiclab.chess.domain.figures.Knight;
import ch.janishuber.logiclab.chess.domain.figures.Bishop;
import ch.janishuber.logiclab.chess.domain.figures.Queen;
import ch.janishuber.logiclab.chess.domain.figures.King;

import java.util.ArrayList;
import java.util.List;

public class BoardInitializerUtil {
    public static ChessBoard Initialize(ChessBoard chessBoard) {
        List<Field> fields = new ArrayList<>();

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                setUpFields(i, j, fields);
            }
        }
        chessBoard.setFields(fields);
        return chessBoard;
    }

    private static void setUpFields(int i, int j, List<Field> fields) {
        Field f = new Field();
        f.row = Character.toString((char) (64 + 9 - i));
        f.column = j;

        if (isRookPosition(f.row, j)) addRook(f, j);
        else if (isPawnPosition(j)) addPawn(f, j);
        else if (isKnightPosition(i, j)) addKnight(f, j);
        else if (isBishopPosition(i, j)) addBishop(f, j);
        else if (isQueenPosition(i, j)) addQueen(f, j);
        else if (isKingPosition(i, j)) addKing(f, j);

        fields.add(f);
    }

    private static void addPawn(Field f, int j) {
        f.figure = new Pawn();
        f.figure.position = f;
        f.figure.figureColor = (j == 2) ? FigureColor.WHITE : FigureColor.BLACK;
    }

    private static void addRook(Field f, int j) {
        f.figure = new Rook();
        f.figure.position = f;
        f.figure.figureColor = (j == 1) ? FigureColor.WHITE : FigureColor.BLACK;
    }

    private static void addKnight(Field f, int j) {
        f.figure = new Knight();
        f.figure.position = f;
        f.figure.figureColor = (j == 1) ? FigureColor.WHITE : FigureColor.BLACK;
    }

    private static void addBishop(Field f, int j) {
        f.figure = new Bishop();
        f.figure.position = f;
        f.figure.figureColor = (j == 1) ? FigureColor.WHITE : FigureColor.BLACK;
    }

    private static void addQueen(Field f, int j) {
        f.figure = new Queen();
        f.figure.position = f;
        f.figure.figureColor = (j == 1) ? FigureColor.WHITE : FigureColor.BLACK;
    }

    private static void addKing(Field f, int j) {
        f.figure = new King();
        f.figure.position = f;
        f.figure.figureColor = (j == 1) ? FigureColor.WHITE : FigureColor.BLACK;
    }

    private static boolean isRookPosition(String row, int column) {
        return (row.equals("H") || row.equals("A")) && (column == 1 || column == 8);
    }

    private static boolean isPawnPosition(int column) {
        return column == 2 || column == 7;
    }

    private static boolean isKnightPosition(int row, int column) {
        return (column == 8 || column == 1) && (row == 2 || row == 7);
    }

    private static boolean isBishopPosition(int row, int column) {
        return (column == 8 || column == 1) && (row == 3 || row == 6);
    }

    private static boolean isQueenPosition(int row, int column) {
        return (column == 8 || column == 1) && row == 4;
    }

    private static boolean isKingPosition(int row, int column) {
        return (column == 8 || column == 1) && row == 5;
    }
}