package ch.janishuber.logiclab.domain.chess.figures;



import ch.janishuber.logiclab.domain.chess.board.ChessBoard;
import ch.janishuber.logiclab.domain.chess.board.Field;
import ch.janishuber.logiclab.domain.chess.util.ChessFigure;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Queen extends ChessFigure {

    private final List<Field> possibleMoves = new ArrayList<>();
    public int value = 9;

    public List<Field> getPossibleMoves(ChessBoard chessBoard) {
        possibleMoves.clear();
        int[][] directions = {
                {0, 1}, {0, -1}, {1, 0}, {-1, 0},
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };

        int startRow = this.position.getRow(); // 1–8
        char startColChar = this.position.getColumn().charAt(0); // 'A'–'H'
        int startCol = startColChar - 'A' + 1;

        for (int[] direction : directions) {
            int row = startRow;
            int col = startCol;

            while (true) {
                row += direction[0];
                col += direction[1];

                if (row < 1 || row > 8 || col < 1 || col > 8) break;

                String colStr = String.valueOf((char) ('A' + col - 1));
                Field field = chessBoard.getField(colStr, row);
                ChessFigure target = field.getFigure();

                if (target == null) {
                    possibleMoves.add(field);
                } else {
                    if (!target.figureColor.equals(this.figureColor)) {
                        possibleMoves.add(field);
                    }
                    break;
                }
            }
        }

        return possibleMoves;
    }

}
