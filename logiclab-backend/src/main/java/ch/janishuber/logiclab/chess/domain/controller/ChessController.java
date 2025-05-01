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

    private boolean publicAgainstAI;
    private int botDifficulty;
    private FigureColor botColor;
    private String moveHistoryGame;
    private transient ChessBot bot;

    public CheckMoveHandler checkMoveHandler;

    public ChessController(boolean againstAI, boolean noInit, FigureColor botColor, int botDifficulty, String moveHistoryGame)
    {
        this.publicAgainstAI = againstAI;
        this.botDifficulty = botDifficulty;
        this.botColor = botColor;
        this.moveHistoryGame = moveHistoryGame;
        if (!noInit) {
            init(botDifficulty);
        }
        if (publicAgainstAI) {
            bot = new ChessBot(botDifficulty - 2, (botDifficulty > 5) ? botDifficulty - 4 : 0);
        }
    }

    public static ChessController ofExisting(ChessBoard chessBoard, FigureColor currentTurn, boolean againstAI, FigureColor botColor, int botDifficulty, String moveHistoryGame) {
        ChessController chessController = new ChessController(againstAI, true, botColor, botDifficulty, moveHistoryGame);
        chessController.chessBoard = chessBoard;
        chessController.currentTurn = currentTurn;
        return chessController;
    }

    private void init(int botDifficulty) {
        chessBoard = BoardInitializerUtil.Initialize(new ChessBoard());
    }

    /**
     * Makes the bot move if the game is against AI and it's the bot's turn.
     * 
     * @return Optional<Move> botMove empty if Bot couldn't make a move.
     */
    public Optional<Move> makeBotMove() {
        if (!publicAgainstAI || currentTurn != this.botColor) {
            return Optional.empty();
        }

        Move bestMove = bot.getBestMove(chessBoard, currentTurn, botColor, moveHistoryGame);
        if (bestMove == null) {
            return Optional.empty();
        }

        Field source = bestMove.getSource(chessBoard);
        Field target = bestMove.getTarget(chessBoard);


        Optional<Boolean> hasMoved = move(source, target);
        if (hasMoved.isPresent() && hasMoved.get()) {
            return Optional.of(bestMove);
        }
        return Optional.empty();
    }

    public Optional<Boolean> move(Field currentField, Field target)
    {
        List<Field> fields = getCheckedMove(currentField.figure);
        if (fields == null) {
            System.out.println("Null");
            for (Field field : chessBoard.getFields()) {
                if (field.figure != null && field.figure.figureColor == currentTurn) {
                    System.out.println(field.figure);
                    if (!getCheckedMove(field.figure).isEmpty()) {
                        System.out.println("Wrong Checkmate");
                    };
                }
            }
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

    public FigureColor getBotColor() {
        return botColor;
    }
}
