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

        int currentRow = this.position.getRow();
        char currentColChar = this.position.getColumn().charAt(0);
        int currentCol = currentColChar - 'A' + 1;

        for (int[] move : moves) {
            int newRow = currentRow + move[0];
            int newCol = currentCol + move[1];

            if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                String newColStr = String.valueOf((char) ('A' + newCol - 1));
                Field field = chessBoard.getField(newColStr, newRow);
                ChessFigure targetFigure = field.getFigure();

                if (targetFigure == null || !targetFigure.figureColor.equals(this.figureColor)) {
                    possibleMoves.add(field);
                }
            }
        }

        return possibleMoves;
    }

}
