package ch.janishuber.logiclab.chess.domain.figures;



import ch.janishuber.logiclab.chess.domain.board.ChessBoard;
import ch.janishuber.logiclab.chess.domain.board.Field;
import ch.janishuber.logiclab.chess.domain.util.ChessFigure;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Bishop extends ChessFigure implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Field> possibleMoves = new ArrayList<>();
    public int value = 3;

    public List<Field> getPossibleMoves(ChessBoard chessBoard) {
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