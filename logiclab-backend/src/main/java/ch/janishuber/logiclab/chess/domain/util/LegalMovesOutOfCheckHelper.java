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
     * @param figure The chess figure making the moveOnChessboard.
     * @return The filtered list of moves that do not leave the king in checkmate.
     */
    public static List<Field> filterMovesPreventingCheck(ChessBoard board, CheckmateHandler checkmateHandler, List<Field> moveList, ChessFigure figure) {
        if (moveList == null || moveList.isEmpty()) return moveList;

        List<Field> validMoves = new ArrayList<>();
        Field sourceField = board.getField(figure.position.getColumn(), figure.position.getRow());
        Field originalPosition = figure.position;

        for (Field target : moveList) {
            ChessFigure capturedFigure = target.getFigure();

            sourceField.setFigure(null);
            target.setFigure(figure);
            figure.position = target;

            boolean kingInCheck = (checkmateHandler.isMate(null) > 0);

            target.setFigure(capturedFigure);
            sourceField.setFigure(figure);
            figure.position = originalPosition;
            if (capturedFigure != null) capturedFigure.position = target;

            if (!kingInCheck) {
                validMoves.add(target);
            }
        }
        return validMoves;
    }

}
