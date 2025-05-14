package ch.janishuber.logiclab.domain.chess.figures;


import ch.janishuber.logiclab.domain.chess.board.ChessBoard;
import ch.janishuber.logiclab.domain.chess.board.Field;
import ch.janishuber.logiclab.domain.chess.util.ChessFigure;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class King extends ChessFigure {

    private final List<Field> possibleMoves = new ArrayList<>();
    public int value = 1000;

    public List<Field> getPossibleMoves(ChessBoard chessBoard) {
        possibleMoves.clear();
        int[][] directions = {
                {0, 1}, {1, 1}, {1, 0}, {1, -1},
                {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}
        };

        int currentRow = this.position.getRow();
        char currentColChar = this.position.getColumn().charAt(0);
        int currentCol = currentColChar - 'A' + 1;

        for (int[] direction : directions) {
            int newRow = currentRow + direction[0];
            int newCol = currentCol + direction[1];

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
