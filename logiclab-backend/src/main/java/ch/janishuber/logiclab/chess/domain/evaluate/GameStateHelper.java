package ch.janishuber.logiclab.chess.domain.evaluate;

import ch.janishuber.logiclab.chess.domain.board.ChessBoard;
import ch.janishuber.logiclab.chess.domain.board.Field;
import ch.janishuber.logiclab.chess.domain.controller.LegalMovesHandler;
import ch.janishuber.logiclab.chess.domain.enums.FigureColor;

import java.util.List;
import java.util.Optional;

public class GameStateHelper {

    private static boolean hasNoLegalMoves(ChessBoard chessBoard, FigureColor currentTurn) {
        LegalMovesHandler legalMovesHandler = new LegalMovesHandler(chessBoard, currentTurn);
        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() != null && field.getFigure().figureColor == currentTurn) {
                List<Field> checkedMove = legalMovesHandler.getLegalMoves(field.getFigure());
                if (checkedMove != null && !checkedMove.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check if the game is in stalemate or checkmate.
     * @return Optional<Boolean> - true if stalemate, false if checkmate, empty if game is still ongoing.
     */
    public static Optional<Boolean> getStalemateStatus(ChessBoard chessBoard, FigureColor currentTurn) {
        LegalMovesHandler legalMovesHandler = new LegalMovesHandler(chessBoard, currentTurn);
        boolean hasNoLegalMoves = hasNoLegalMoves(chessBoard, currentTurn);

        if (!hasNoLegalMoves) {
            return Optional.empty();
        }

        if (legalMovesHandler.checkmateHandler.isMate(null) > 0) {
            return Optional.of(false);
        }

        return Optional.of(true);
    }
}
