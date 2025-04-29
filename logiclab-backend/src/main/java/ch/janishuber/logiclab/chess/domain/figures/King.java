package org.example.chess.backend.figures;
import org.example.chess.backend.util.ChessFigure;
import org.example.chess.backend.board.Field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class King extends ChessFigure implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Field> possibleMoves = new ArrayList<>();
    private boolean hasMoved = false;
    public int value = 1000;

    public List<Field> getPossibleMoves() {
        possibleMoves.clear();
        int[][] directions = {
                {0, 1}, {1, 1}, {1, 0}, {1, -1},
                {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}
        };

        for (int[] direction : directions) {
            int newRow = this.position.getRowInt() + direction[0];
            int newCol = this.position.column + direction[1];

            if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                Field field = chessBoard.getField(Character.toString((char) (newRow + 64)), newCol);
                if (field.figure == null || field.figure.figureColor != this.figureColor) {
                    possibleMoves.add(field);
                }
            }
        }
        return possibleMoves;
    }
}
