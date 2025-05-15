package ch.janishuber.logiclab.domain.chess.util;

import ch.janishuber.logiclab.domain.chess.board.ChessBoard;
import ch.janishuber.logiclab.domain.chess.board.Field;
import ch.janishuber.logiclab.domain.chess.controller.LegalMovesHandler;
import ch.janishuber.logiclab.domain.chess.enums.FigureColor;

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

        if (!hasEnoughMaterial(chessBoard)) {
            return Optional.of(true);
        }

        return Optional.of(true);
    }

    public static boolean hasEnoughMaterial(ChessBoard board) {
        int whiteCount = 0;
        int blackCount = 0;
        boolean whiteHasOnlyMinor = true;
        boolean blackHasOnlyMinor = true;

        for (Field field : board.getFields()) {
            ChessFigure fig = field.getFigure();
            if (fig == null) continue;

            boolean isMinorPiece = fig.getClassName().equals("Knight") || fig.getClassName().equals("Bishop");
            boolean isKing = fig.getClassName().equals("King");

            if (fig.figureColor == FigureColor.WHITE) {
                if (!isMinorPiece && !isKing) whiteHasOnlyMinor = false;
                whiteCount++;
            } else {
                if (!isMinorPiece && !isKing) blackHasOnlyMinor = false;
                blackCount++;
            }
        }

        if (whiteCount == 1 && blackCount == 1) return false;
        if (whiteHasOnlyMinor && whiteCount <= 2 && blackHasOnlyMinor && blackCount <= 2) return true;
        return false;
    }

}
