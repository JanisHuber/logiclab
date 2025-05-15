package ch.janishuber.logiclab.domain.chess.controller;


import ch.janishuber.logiclab.domain.chess.board.ChessBoard;
import ch.janishuber.logiclab.domain.chess.board.Field;
import ch.janishuber.logiclab.domain.chess.bot.ChessBot;
import ch.janishuber.logiclab.domain.chess.enums.FigureColor;
import ch.janishuber.logiclab.domain.chess.figures.Bishop;
import ch.janishuber.logiclab.domain.chess.figures.Knight;
import ch.janishuber.logiclab.domain.chess.figures.Queen;
import ch.janishuber.logiclab.domain.chess.figures.Rook;
import ch.janishuber.logiclab.domain.chess.util.BoardInitializerUtil;
import ch.janishuber.logiclab.domain.chess.util.ChessFigure;
import ch.janishuber.logiclab.domain.chess.util.Move;
import java.util.List;
import java.util.Optional;

public class ChessController {

    public ChessBoard chessBoard;
    public FigureColor currentTurn = FigureColor.WHITE;

    private final boolean publicAgainstAI;
    private final FigureColor botColor;
    private final String moveHistoryGame;
    private transient ChessBot bot;
    private static final String KING_CLASS_NAME = "King";
    private static final String PAWN_CLASS_NAME = "Pawn";

    public LegalMovesHandler legalMovesHandler;

    private ChessController(boolean againstAI, boolean noInit, FigureColor botColor, String moveHistoryGame, int botDifficulty) {
        this.publicAgainstAI = againstAI;
        this.botColor = botColor;
        this.moveHistoryGame = moveHistoryGame;
        if (!noInit) {
            init();
        }
        if (publicAgainstAI) {
            //bot = new ChessBot(botDifficulty - 2, (botDifficulty > 5) ? botDifficulty - 4 : 0);
            bot = new ChessBot(3, 3);
        }
    }

    public static ChessController ofExisting(ChessBoard chessBoard, FigureColor currentTurn, boolean againstAI, FigureColor botColor, String moveHistoryGame, int botDifficultly) {
        ChessController chessController = new ChessController(againstAI, true, botColor, moveHistoryGame, botDifficultly);
        chessController.chessBoard = chessBoard;
        chessController.currentTurn = currentTurn;
        return chessController;
    }

    public static ChessController startNewGame(boolean againstAI, FigureColor botColor, String moveHistoryGame, int botDifficulty) {
        return new ChessController(againstAI, false, botColor, moveHistoryGame, botDifficulty);
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

    public boolean moveOnChessboard(Move move, String promotingFigureClassName) {
        Field currentField = move.getSource(chessBoard);
        Field target = move.getTarget(chessBoard);

        List<Field> fields = getLegalMoves(currentField.getFigure());

        if (fields.contains(target)) {
            applyMove(currentField, target, promotingFigureClassName);
            return true;
        }
        return false;
    }

    public List<Field> getLegalMoves(ChessFigure figure) {
        legalMovesHandler = new LegalMovesHandler(chessBoard, currentTurn);
        List<Field> legalMoves = legalMovesHandler.getLegalMoves(figure);

        if (figure.getClassName().equals(KING_CLASS_NAME)) {
            legalMoves.addAll(legalMovesHandler.getLegalCastlingMoves(figure));
        }
        return legalMoves;
    }

    private void applyMove(Field currentField, Field target, String promotingFigureClassName) {
        if (currentField.getFigure().getClassName().equals(KING_CLASS_NAME)) {
            if (currentField.getColumn().equals("E") && target.getColumn().equals("G")) {
                applyCastling(currentField, target);
                return;
            } else if (currentField.getColumn().equals("E") && target.getColumn().equals("C")) {
                applyCastling(currentField, target);
                return;
            }
        }
        if (currentField.getFigure().getClassName().equals(PAWN_CLASS_NAME) && (target.getRow() == 1 || target.getRow() == 8)) {
            promotePawn(currentField, target, promotingFigureClassName);
            return;
        }
        if (target.getFigure() != null) {
            target.getFigure().position = null;
        }
        currentField.getFigure().position = target;
        target.setFigure(currentField.getFigure());
        currentField.setFigure(null);

        currentTurn = currentTurn == FigureColor.WHITE ? FigureColor.BLACK : FigureColor.WHITE;
        target.getFigure().hasMoved = true;
    }

    private void applyCastling(Field currentField, Field target) {
        currentField.getFigure().position = target;
        target.setFigure(currentField.getFigure());
        currentField.setFigure(null);
        if (target.getColumn().equals("G")) {
            Field rookField = chessBoard.getField("H", target.getRow());
            Field newRookField = chessBoard.getField("F", target.getRow());
            newRookField.setFigure(rookField.getFigure());
            rookField.setFigure(null);
            newRookField.getFigure().position = newRookField;
        } else if (target.getColumn().equals("C")) {
            Field rookField = chessBoard.getField("A", target.getRow());
            Field newRookField = chessBoard.getField("D", target.getRow());
            newRookField.setFigure(rookField.getFigure());
            rookField.setFigure(null);
            newRookField.getFigure().position = newRookField;
        }

        currentTurn = currentTurn == FigureColor.WHITE ? FigureColor.BLACK : FigureColor.WHITE;
        target.getFigure().hasMoved = true;
    }

    private void promotePawn(Field currentField, Field target, String promotingFigureClassName) {
        System.out.println("Promoting pawn to: " + promotingFigureClassName);
        if (target.getFigure() != null) {
            target.getFigure().position = null;
        }
        switch(promotingFigureClassName) {
            case "Queen":
                target.setFigure(new Queen());
                break;
            case "Rook":
                target.setFigure(new Rook());
                break;
            case "Bishop":
                target.setFigure(new Bishop());
                break;
            case "Knight":
                target.setFigure(new Knight());
                break;
            default:
                throw new IllegalArgumentException("Invalid figure class name for promotion: " + promotingFigureClassName);
        }
        target.getFigure().position = target;
        target.getFigure().figureColor = currentField.getFigure().figureColor;
        currentField.getFigure().position = null;
        currentField.setFigure(null);

        currentTurn = (currentTurn == FigureColor.WHITE) ? FigureColor.BLACK : FigureColor.WHITE;
        target.getFigure().hasMoved = true;
    }

    public FigureColor getBotColor() {
        return botColor;
    }
}
