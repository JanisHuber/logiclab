package ch.janishuber.logiclab.chess.domain.figures;


import ch.janishuber.logiclab.chess.domain.board.ChessBoard;
import ch.janishuber.logiclab.chess.domain.board.Field;
import ch.janishuber.logiclab.chess.domain.enums.FigureColor;
import ch.janishuber.logiclab.chess.domain.util.ChessFigure;
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

        for (Field field : chessBoard.getFields()) {
            if (field.row.equals(this.position.row) && field.column == (this.position.column + direction)) {
                if (field.figure == null) {
                    possibleMoves.add(field);
                }
            }
            if (field.row.equals(this.position.row) && field.column == (this.position.column + 2 * direction)) {
                if (field.figure == null && this.position.column == startRow) {
                    Field intermediateField = chessBoard.getField(this.position.row, this.position.column + direction);
                    if (intermediateField.figure == null) {
                        possibleMoves.add(field);
                    }
                }
            }
            if (field.column == (this.position.column + direction)) {
                if (field.row.equals(String.valueOf((char) (this.position.row.charAt(0) + 1))) ||
                    field.row.equals(String.valueOf((char) (this.position.row.charAt(0) - 1)))) {
                    if (field.figure != null && field.figure.figureColor != this.figureColor) {
                        possibleMoves.add(field);
                    }
                }
            }
        }
        return possibleMoves;
    }
}