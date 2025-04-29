package org.example.chess.backend.figures;

import org.example.chess.backend.util.ChessFigure;
import org.example.chess.backend.board.Field;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Bishop extends ChessFigure implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Field> possibleMoves = new ArrayList<>();
    public int value = 3;

    public List<Field> getPossibleMoves() {
        possibleMoves.clear();
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        for (int[] direction : directions) {
            int rowOffset = direction[0];
            int colOffset = direction[1];

            for (int i = 1;
                 this.position.column + i * colOffset >= 1 && this.position.column + i * colOffset <= 8 &&
                 this.position.getRowInt() + i * rowOffset >= 1 && this.position.getRowInt() + i * rowOffset <= 8;
                 i++) {
                Field field = chessBoard.getField(
                    Character.toString((char) (this.position.getRowInt() + i * rowOffset + 64)),
                    this.position.column + i * colOffset
                );

                if (field.figure == null) {
                    possibleMoves.add(field);
                } else {
                    if (field.figure.figureColor != this.figureColor) {
                        possibleMoves.add(field);
                    }
                    break;
                }
            }
        }
        return possibleMoves;
    }
}