package ch.janishuber.logiclab.domain.chess.figures;

import ch.janishuber.logiclab.domain.chess.board.ChessBoard;
import ch.janishuber.logiclab.domain.chess.board.Field;
import ch.janishuber.logiclab.domain.chess.util.ChessFigure;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Rook extends ChessFigure {

    private List<Field> possibleMoves = new ArrayList<>();
    public int value = 5;

    public List<Field> getPossibleMoves(ChessBoard chessBoard) {
        possibleMoves.clear();

        int[][] directions = {
                { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 }
        };

        int startRow = this.position.getRow();
        char startColChar = this.position.getColumn().charAt(0);
        int startCol = startColChar - 'A';

        for (int[] direction : directions) {
            int row = startRow;
            int col = startCol;

            while (true) {
                row += direction[0];
                col += direction[1];

                if (row < 1 || row > 8 || col < 0 || col > 7)
                    break;

                String colStr = String.valueOf((char) ('A' + col));
                Field field = chessBoard.getField(colStr, row);
                ChessFigure figure = field.getFigure();

                if (figure == null) {
                    possibleMoves.add(field);
                } else {
                    if (!figure.figureColor.equals(this.figureColor)) {
                        possibleMoves.add(field);
                    }
                    break;
                }
            }
        }
        return possibleMoves;
    }
}