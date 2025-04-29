package ch.janishuber.logiclab.chess.domain.controller;


import ch.janishuber.logiclab.chess.domain.board.ChessBoard;
import ch.janishuber.logiclab.chess.domain.board.Field;
import ch.janishuber.logiclab.chess.domain.bot.ChessBot;
import ch.janishuber.logiclab.chess.domain.enums.FigureColor;
import ch.janishuber.logiclab.chess.domain.util.BoardInitializerUtil;
import ch.janishuber.logiclab.chess.domain.util.ChessFigure;
import ch.janishuber.logiclab.chess.domain.util.Move;

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

    public ChessController(boolean againstAI, boolean noInit)
    {
        publicAgainstAI = againstAI;
        if (!noInit) {
            init();
            if (publicAgainstAI) {
                Move botMove = bot.getBestMove(this);
                move(botMove.getSource(chessBoard), botMove.getTarget(chessBoard));
            }
        }
    }

    public static ChessController ofExisting(ChessBoard chessBoard, FigureColor currentTurn) {
        ChessController chessController = new ChessController(true, true);
        chessController.chessBoard = chessBoard;
        chessController.currentTurn = currentTurn;
        return chessController;
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

    /**
     * Makes the bot move if the game is against AI and it's the bot's turn.
     * @return Optional<Boolean> - true if the bot made a move, false if it didn't.
     */
    public Optional<Boolean> makeBotMove() {
        ChessBot bot = new ChessBot(3, 0);
        Optional<Boolean> hasMoved = Optional.of(false);
        if (publicAgainstAI && currentTurn == FigureColor.WHITE) {
            Move botMove = bot.getBestMove(this);
            hasMoved = move(botMove.getSource(chessBoard), botMove.getTarget(chessBoard));
        }
        return hasMoved;
    }

    public Move getBotMove() {
        return bot.getBestMove(this);
    }

    public Optional<Boolean> move(Field currentField, Field target)
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
