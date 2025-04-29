package ch.janishuber.logiclab.chess.domain.figures;



import ch.janishuber.logiclab.chess.domain.board.ChessBoard;
import ch.janishuber.logiclab.chess.domain.board.Field;
import ch.janishuber.logiclab.chess.domain.util.ChessFigure;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Knight extends ChessFigure implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Field> possibleMoves = new ArrayList<>();
    public int value = 3;

    public List<Field> getPossibleMoves(ChessBoard chessBoard) {
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
