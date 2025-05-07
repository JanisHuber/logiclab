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
    private final FigureColor botColor;
    private final String moveHistoryGame;
    private transient ChessBot bot;

    public LegalMovesHandler legalMovesHandler;

    private ChessController(boolean againstAI, boolean noInit, FigureColor botColor, String moveHistoryGame) {
        this.publicAgainstAI = againstAI;
        this.botColor = botColor;
        this.moveHistoryGame = moveHistoryGame;
        if (!noInit) {
            init();
        }
        if (publicAgainstAI) {
            //bot = new ChessBot(botDifficulty - 2, (botDifficulty > 5) ? botDifficulty - 4 : 0);
            bot = new ChessBot(3, 10);
        }
    }

    public static ChessController ofExisting(ChessBoard chessBoard, FigureColor currentTurn, boolean againstAI, FigureColor botColor, String moveHistoryGame) {
        ChessController chessController = new ChessController(againstAI, true, botColor, moveHistoryGame);
        chessController.chessBoard = chessBoard;
        chessController.currentTurn = currentTurn;
        return chessController;
    }

    public static ChessController startNewGame(boolean againstAI, FigureColor botColor, String moveHistoryGame) {
        return new ChessController(againstAI, false, botColor, moveHistoryGame);
    }

    private void init() {
        chessBoard = BoardInitializerUtil.Initialize(new ChessBoard());
    }

    /**
     * Makes the bot moveOnChessboard if the game is against AI and it's the bot's turn.
     * 
     * @return Optional<Move> botMove empty if Bot couldn't make a moveOnChessboard.
     */
    public Optional<Move> getBotMove() {
        if (!publicAgainstAI || currentTurn != this.botColor) {
            return Optional.empty();
        }

        Move bestMove = bot.getBestMove(chessBoard, currentTurn, botColor, moveHistoryGame);
        if (bestMove == null) {
            return Optional.empty();
        }
        return Optional.of(bestMove);
    }

    public boolean moveOnChessboard(Move move) {
        Field currentField = move.getSource(chessBoard);
        Field target = move.getTarget(chessBoard);

        List<Field> fields = getLegalMoves(currentField.getFigure());

        if (fields.contains(target)) {
            applyMove(currentField, target);
            return true;
        }
        return false;
    }

    public List<Field> getLegalMoves(ChessFigure figure) {
        legalMovesHandler = new LegalMovesHandler(chessBoard, currentTurn);

        return legalMovesHandler.getLegalMoves(figure);
    }

    private void applyMove(Field currentField, Field target) {
        if (target.getFigure() != null) {
            target.getFigure().position = null;
        }
        currentField.getFigure().position = target;
        target.setFigure(currentField.getFigure());
        currentField.setFigure(null);

        currentTurn = currentTurn == FigureColor.WHITE ? FigureColor.BLACK : FigureColor.WHITE;
    }

    public FigureColor getBotColor() {
        return botColor;
    }
}
