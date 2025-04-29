package org.example.chess.backend.figures;

import org.example.chess.backend.util.ChessFigure;
import org.example.chess.backend.board.Field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Knight extends ChessFigure implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Field> possibleMoves = new ArrayList<>();
    public int value = 3;

    public List<Field> getPossibleMoves() {
                    possibleMoves.clear();
                    int[][] moves = {
                        {2, 1}, {1, 2}, {-1, 2}, {-2, 1},
                        {-2, -1}, {-1, -2}, {1, -2}, {2, -1}
                    };

                    for (int[] move : moves) {
                        int newRow = this.position.getRowInt() + move[0];
                        int newCol = this.position.column + move[1];

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
