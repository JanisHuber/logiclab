package ch.janishuber.logiclab.domain.chess.figures;



import ch.janishuber.logiclab.domain.chess.board.ChessBoard;
import ch.janishuber.logiclab.domain.chess.board.Field;
import ch.janishuber.logiclab.domain.chess.util.ChessFigure;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Bishop extends ChessFigure {

    private final List<Field> possibleMoves = new ArrayList<>();
    public int value = 3;

    public List<Field> getPossibleMoves(ChessBoard chessBoard) {
        possibleMoves.clear();
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        int currentRow = this.position.getRow();
        char currentColChar = this.position.getColumn().charAt(0);
        int currentCol = currentColChar - 'A' + 1;

        for (int[] direction : directions) {
            int rowOffset = direction[0];
            int colOffset = direction[1];

            for (int i = 1; currentRow + i * rowOffset >= 1 && currentRow + i * rowOffset <= 8 && currentCol + i * colOffset >= 1 && currentCol + i * colOffset <= 8; i++) {
                int targetRow = currentRow + i * rowOffset;
                int targetCol = currentCol + i * colOffset;
                String targetColStr = String.valueOf((char) ('A' + targetCol - 1));

                Field field = chessBoard.getField(targetColStr, targetRow);
                ChessFigure targetFigure = field.getFigure();

                if (targetFigure == null) {
                    possibleMoves.add(field);
                } else {
                    if (!targetFigure.figureColor.equals(this.figureColor)) {
                        possibleMoves.add(field);
                    }
                    break;
                }
            }
        }
        return possibleMoves;
    }

}