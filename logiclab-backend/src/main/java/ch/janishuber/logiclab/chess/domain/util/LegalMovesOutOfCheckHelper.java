package org.example.chess.backend.util;

import org.example.chess.backend.board.ChessBoard;
import org.example.chess.backend.board.Field;
import org.example.chess.backend.controller.CheckmateHandler;

import java.util.ArrayList;
import java.util.List;

public class LegalMovesOutOfCheckHelper {

    /**
     * Filters out moves that would leave the king in checkmate.
     *
     * @param sourceList The list of possible moves to filter.
     * @param figure The chess figure making the move.
     * @return The filtered list of moves that do not leave the king in checkmate.
     */
    public static List<Field> filterMovesPreventingCheckmate(ChessBoard chessBoard, CheckmateHandler checkmateHandler, List<Field> sourceList, ChessFigure figure) {
        if (sourceList == null || sourceList.isEmpty()) {
            return sourceList;
        }
        List<Field> invalidMoves = new ArrayList<>();

        for (Field targetField : sourceList) {
            ChessFigure originalTargetFigure = targetField.figure;
            boolean isInCheck;

            Field figureSourceField = chessBoard.getField(figure.position.row, figure.position.column);

            if (figureSourceField.figure != null) {
                figureSourceField.figure.position = null;
            }
            figureSourceField.figure = null;


            targetField.figure = figure;
            figure.position = targetField;

            isInCheck = checkmateHandler.isMate(null) > 0;

            targetField.figure = originalTargetFigure;
            if (originalTargetFigure != null) {
                originalTargetFigure.position = targetField;
            }

            figureSourceField.figure = figure;
            figure.position = figureSourceField;

            if (isInCheck) {
                invalidMoves.add(targetField);
            }
        }
        sourceList.removeAll(invalidMoves);
        return sourceList;
    }
}
