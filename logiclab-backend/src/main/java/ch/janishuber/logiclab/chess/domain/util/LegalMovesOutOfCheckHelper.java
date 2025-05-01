package ch.janishuber.logiclab.chess.domain.util;


import ch.janishuber.logiclab.chess.domain.board.ChessBoard;
import ch.janishuber.logiclab.chess.domain.board.Field;
import ch.janishuber.logiclab.chess.domain.controller.CheckmateHandler;

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
            ChessFigure originalTargetFigure = targetField.getFigure();
            boolean isInCheck;

            Field figureSourceField = chessBoard.getField(figure.position.getColumn(), figure.position.getRow());

            if (figureSourceField.getFigure() != null) {
                figureSourceField.getFigure().position = null;
            }
            figureSourceField.getFigure().position = null;


            targetField.setFigure(figure);
            figure.position = targetField;

            isInCheck = checkmateHandler.isMate(null) > 0;

            targetField.setFigure(originalTargetFigure);
            if (originalTargetFigure != null) {
                originalTargetFigure.position = targetField;
            }

            figureSourceField.setFigure(figure);
            figure.position = figureSourceField;

            if (isInCheck) {
                invalidMoves.add(targetField);
            }
        }
        sourceList.removeAll(invalidMoves);
        return sourceList;
    }
}
