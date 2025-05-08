package ch.janishuber.logiclab.domain.chess.figures;


import ch.janishuber.logiclab.domain.chess.board.ChessBoard;
import ch.janishuber.logiclab.domain.chess.board.Field;
import ch.janishuber.logiclab.domain.chess.enums.FigureColor;
import ch.janishuber.logiclab.domain.chess.util.ChessFigure;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pawn extends ChessFigure implements Serializable {
    private static final long serialVersionUID = 1L;

    public List<Field> getPossibleMoves(ChessBoard chessBoard) {
        List<Field> possibleMoves = new ArrayList<>();
        int direction = (this.figureColor == FigureColor.WHITE) ? 1 : -1;
        int startRow = (this.figureColor == FigureColor.WHITE) ? 2 : 7;

        int currentRow = this.position.getRow();
        String currentCol = this.position.getColumn();
        char currentColChar = currentCol.charAt(0);

        int oneStepForward = currentRow + direction;

        if (oneStepForward >= 1 && oneStepForward <= 8) {
            Field forwardField = chessBoard.getField(currentCol, oneStepForward);
            if (forwardField.getFigure() == null) {
                possibleMoves.add(forwardField);

                int twoStepsForward = currentRow + 2 * direction;
                if (currentRow == startRow && twoStepsForward >= 1 && twoStepsForward <= 8) {
                    Field twoStepField = chessBoard.getField(currentCol, twoStepsForward);
                    if (twoStepField.getFigure() == null) {
                        possibleMoves.add(twoStepField);
                    }
                }
            }
        }

        for (int colOffset : new int[]{-1, 1}) {
            char diagColChar = (char) (currentColChar + colOffset);
            if (diagColChar >= 'A' && diagColChar <= 'H') {
                String diagCol = String.valueOf(diagColChar);
                Field diagField = chessBoard.getField(diagCol, oneStepForward);
                if (diagField != null && diagField.getFigure() != null &&
                        !diagField.getFigure().figureColor.equals(this.figureColor)) {
                    possibleMoves.add(diagField);
                }
            }
        }

        return possibleMoves;
    }

}