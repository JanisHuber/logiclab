package org.example.chess.backend.util;

import javafx.scene.image.Image;
import org.example.chess.backend.board.ChessBoard;
import org.example.chess.backend.board.Field;
import org.example.chess.backend.enums.FigureColor;
import org.example.chess.backend.figures.*;


import java.util.ArrayList;
import java.util.List;

public class BoardInitializerUtil {

    public static ChessBoard Initialize(ChessBoard chessBoard) {
        List<Field> fields = new ArrayList<>();

        for (int i = 1; i <= 8; i++)
        {
            for (int j = 1; j <= 8; j++)
            {
                Field f = new Field();
                f.row = Character.toString((char)(64 + 9 - i));
                f.column = j;

                if ((f.row.equals("H") || f.row.equals("A")) && (j == 1 || j == 8)) // Rook
                {
                    f.figure = new Rook();
                    f.figure.position = f;
                    f.figure.chessBoard = chessBoard;
                    if (j == 1) {
                        f.figure.figureColor = FigureColor.WHITE;
                        f.figure.image = new Image(BoardInitializerUtil.class.getResource("/images/figure_white_rook.png").toExternalForm());
                    } else {
                        f.figure.figureColor = FigureColor.BLACK;
                        f.figure.image = new Image(BoardInitializerUtil.class.getResource("/images/figure_black_rook.png").toExternalForm());
                    }
                }

                if (j == 2 || j == 7) // Pawn
                {
                    f.figure = new Pawn();
                    f.figure.position = f;
                    f.figure.chessBoard = chessBoard;
                    if (j == 2) {
                        f.figure.figureColor = FigureColor.WHITE;
                        f.figure.image = new Image(BoardInitializerUtil.class.getResource("/images/figure_white_pawn.png").toExternalForm());
                    } else {
                        f.figure.figureColor = FigureColor.BLACK;
                        f.figure.image = new Image(BoardInitializerUtil.class.getResource("/images/figure_black_pawn.png").toExternalForm());
                    }
                }

                if ((j == 8 || j == 1) && (i == 2 || i == 7)) // Knight
                {
                    f.figure = new Knight();
                    f.figure.position = f;
                    f.figure.chessBoard = chessBoard;
                    if (j == 1)
                    {
                        f.figure.figureColor = FigureColor.WHITE;
                        f.figure.image = new Image(BoardInitializerUtil.class.getResource("/images/figure_white_knight.png").toExternalForm());
                    } else {
                        f.figure.figureColor = FigureColor.BLACK;
                        f.figure.image = new Image(BoardInitializerUtil.class.getResource("/images/figure_black_knight.png").toExternalForm());
                    }
                }

                if ((j == 8 || j == 1) && (i == 3 || i == 6)) // Bishop
                {
                    f.figure = new Bishop();
                    f.figure.position = f;
                    f.figure.chessBoard = chessBoard;
                    if (j == 1)
                    {
                        f.figure.figureColor = FigureColor.WHITE;
                        f.figure.image = new Image(BoardInitializerUtil.class.getResource("/images/figure_white_bishop.png").toExternalForm());
                    } else {
                        f.figure.figureColor = FigureColor.BLACK;
                        f.figure.image = new Image(BoardInitializerUtil.class.getResource("/images/figure_black_bishop.png").toExternalForm());
                    }
                }

                if ((j == 8 || j == 1) && i == 4) // Queen
                {
                    f.figure = new Queen();
                    f.figure.position = f;
                    f.figure.chessBoard = chessBoard;
                    if (j == 1)
                    {
                        f.figure.figureColor = FigureColor.WHITE;
                        f.figure.image = new Image(BoardInitializerUtil.class.getResource("/images/figure_white_queen.png").toExternalForm());
                    } else {
                        f.figure.figureColor = FigureColor.BLACK;
                        f.figure.image = new Image(BoardInitializerUtil.class.getResource("/images/figure_black_queen.png").toExternalForm());
                    }
                }

                if ((j == 8 || j == 1) && i == 5) // King
                {
                    f.figure = new King();
                    f.figure.position = f;
                    f.figure.chessBoard = chessBoard;
                    if (j == 1)
                    {
                        f.figure.figureColor = FigureColor.WHITE;
                        f.figure.image = new Image(BoardInitializerUtil.class.getResource("/images/figure_white_king.png").toExternalForm());
                    } else {
                        f.figure.figureColor = FigureColor.BLACK;
                        f.figure.image = new Image(BoardInitializerUtil.class.getResource("/images/figure_black_king.png").toExternalForm());
                    }
                }
                fields.add(f);
            }
        }
        chessBoard.setFields(fields);
        return chessBoard;
    }
}
