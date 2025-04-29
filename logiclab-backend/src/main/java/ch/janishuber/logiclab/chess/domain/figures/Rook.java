package ch.janishuber.logiclab.chess.domain.figures;

import ch.janishuber.logiclab.chess.domain.board.ChessBoard;
import ch.janishuber.logiclab.chess.domain.board.Field;
import ch.janishuber.logiclab.chess.domain.util.ChessFigure;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Rook extends ChessFigure implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Field> possibleMoves = new ArrayList<>();
    public int value = 5;

    public List<Field> getPossibleMoves(ChessBoard chessBoard) {
            possibleMoves.clear();
            int[][] directions = {
                {0, 1}, {0, -1}, {1, 0}, {-1, 0}
            };

            for (int[] direction : directions) {
                int row = this.position.getRowInt();
                int col = this.position.column;

                while (true) {
                    row += direction[0];
                    col += direction[1];

                    if (row < 1 || row > 8 || col < 1 || col > 8) break;

                    Field field = chessBoard.getField(Character.toString((char) (row + 64)), col);
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