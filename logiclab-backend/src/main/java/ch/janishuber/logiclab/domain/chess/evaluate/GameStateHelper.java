package ch.janishuber.logiclab.domain.chess.evaluate;

import ch.janishuber.logiclab.domain.chess.board.ChessBoard;
import ch.janishuber.logiclab.domain.chess.board.Field;
import ch.janishuber.logiclab.domain.chess.controller.LegalMovesHandler;
import ch.janishuber.logiclab.domain.chess.enums.FigureColor;
import ch.janishuber.logiclab.domain.chess.figures.Knight;
import ch.janishuber.logiclab.domain.chess.util.ChessFigure;

import java.util.ArrayList;
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

        if (hasEnoughMaterial(chessBoard)) {
            return Optional.of(true);
        }

        if (!hasNoLegalMoves) {
            return Optional.empty();
        }

        if (legalMovesHandler.checkmateHandler.isMate(null) > 0) {
            return Optional.of(false);
        }

        return Optional.of(true);
    }

    public static boolean hasEnoughMaterial(ChessBoard chessBoard) {
        List<ChessFigure> whitePieces = new ArrayList<>();
        List<ChessFigure> blackPieces = new ArrayList<>();

        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() != null) {
                if (field.getFigure().figureColor == FigureColor.WHITE) {
                    whitePieces.add(field.getFigure());
                } else {
                    blackPieces.add(field.getFigure());
                }
            }
        }
        return isOnlyKingAndKnight(whitePieces, blackPieces);
    }

    private static boolean isOnlyKingAndKnight(List<ChessFigure> whitePieces, List<ChessFigure> blackPieces) {
        boolean isStalemate = false;
        if (whitePieces.size() <= 2) {
            for (ChessFigure piece : whitePieces) {
                isStalemate = piece instanceof Knight || piece.getClassName().equals("Bishop") || piece.getClassName().equals("King");
            }
        }
        if (blackPieces.size() <= 2) {
            for (ChessFigure piece : blackPieces) {
                isStalemate = piece instanceof Knight || piece.getClassName().equals("Bishop") || piece.getClassName().equals("King");
            }
        }
        return isStalemate;
    }
}
