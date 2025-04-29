package org.example.chess.backend.util;

import javafx.scene.image.Image;
import org.example.chess.backend.board.ChessBoard;
import org.example.chess.backend.board.Field;
import org.example.chess.backend.enums.FigureColor;

import java.io.Serializable;
import java.util.List;

public abstract class ChessFigure implements Serializable {
    private static final long serialVersionUID = 1L;

    public transient Image image;
    public FigureColor figureColor;
    public Field position;
    public ChessBoard chessBoard;
    public int value;

    public abstract List<Field> getPossibleMoves();

    public String getClassName() {
        return this.getClass().getSimpleName();
    }
}
