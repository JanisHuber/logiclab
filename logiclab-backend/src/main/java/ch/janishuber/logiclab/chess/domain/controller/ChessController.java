package org.example.chess.backend.controller;
import org.example.chess.backend.board.ChessBoard;
import org.example.chess.backend.board.Field;
import org.example.chess.backend.bot.ChessBot;
import org.example.chess.backend.enums.FigureColor;
import org.example.chess.backend.util.BoardInitializerUtil;
import org.example.chess.backend.util.ChessFigure;
import org.example.chess.backend.util.Move;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class ChessController implements Serializable {
    private static final long serialVersionUID = 1L;

    public ChessBoard chessBoard;
    public FigureColor currentTurn = FigureColor.WHITE;

    private final boolean publicAgainstAI;
    private transient ChessBot bot;

    public CheckMoveHandler checkMoveHandler;

    public ChessController(boolean againstAI)
    {
        publicAgainstAI = againstAI;
        init();
        if (publicAgainstAI) {
            Move botMove = bot.getBestMove(this);
            Move(botMove.getSource(chessBoard), botMove.getTarget(chessBoard));
        }
    }

    private void init() {
        if (publicAgainstAI) {
            bot = new ChessBot(3, 0);
        }
        chessBoard = BoardInitializerUtil.Initialize(new ChessBoard());
    }

    private boolean hasNoLegalMoves() {
        checkMoveHandler = new CheckMoveHandler(chessBoard, currentTurn);
        for (Field field : chessBoard.getFields()) {
            if (field.figure != null && field.figure.figureColor == currentTurn) {
                List<Field> checkedMove = checkMoveHandler.getCheckedMove(field.figure);
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
    public Optional<Boolean> getStalemateStatus() {
        checkMoveHandler = new CheckMoveHandler(chessBoard, currentTurn);
        boolean hasNoLegalMoves = hasNoLegalMoves();

        if (!hasNoLegalMoves) {
            return Optional.empty();
        }

        if (checkMoveHandler.checkmateHandler.isMate(null) > 0) {
            return Optional.of(false);
        }

        return Optional.of(true);
    }


    public boolean getBotMove() {
        if (publicAgainstAI && currentTurn == FigureColor.WHITE) {
            Move botMove = bot.getBestMove(this);
            Move(botMove.getSource(chessBoard), botMove.getTarget(chessBoard));
            return true;
        }
        return false;
    }

    public Optional<Boolean> Move(Field currentField, Field target)
    {
        List<Field> fields = getCheckedMove(currentField.figure);
        if (fields == null) {
            System.out.println("Checkmate");
            return Optional.empty();
        }
        if (!fields.contains(target)) {
            return Optional.of(false);
        } else {
            applyMove(currentField, target);
            return Optional.of(true);
        }
    }

    public CheckMoveHandler getCheckMoveHandler() {
        return new CheckMoveHandler(chessBoard, currentTurn);
    }

    public List<Field> getCheckedMove(ChessFigure figure) {
        checkMoveHandler = new CheckMoveHandler(chessBoard, currentTurn);

        return checkMoveHandler.getCheckedMove(figure);
    }

    private void applyMove(Field currentField, Field target) {
        if (target.figure != null)
        {
            target.figure.position = null;
        }
        currentField.figure.position = target;
        target.figure = currentField.figure;
        currentField.figure = null;

        currentTurn = currentTurn == FigureColor.WHITE ? FigureColor.BLACK : FigureColor.WHITE;
    }
}
