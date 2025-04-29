package ch.janishuber.logiclab.chess.domain.bot;

import ch.janishuber.logiclab.chess.domain.board.ChessBoard;
import ch.janishuber.logiclab.chess.domain.board.Field;
import ch.janishuber.logiclab.chess.domain.controller.ChessController;
import ch.janishuber.logiclab.chess.domain.enums.FigureColor;
import ch.janishuber.logiclab.chess.domain.evaluate.evaluateBoard;
import ch.janishuber.logiclab.chess.domain.util.LoggingToFile;
import ch.janishuber.logiclab.chess.domain.util.Move;
import ch.janishuber.logiclab.chess.domain.util.SerializationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@SuppressWarnings("ALL")
public class ChessBot {
    private final int depth;
    private final int maxQuiescenceSearchDepth;
    private final Logger logger = LoggingToFile.getLogger(ChessBot.class.getName());

    public ChessBot(int depth, int maxQuiescenceSearchDepth) {
        this.depth = depth;
        this.maxQuiescenceSearchDepth = maxQuiescenceSearchDepth;
    }

    public Move getBestMove(ChessController controller) {
        Move bestMove = null;
        int maxEval = Integer.MIN_VALUE;

        List<Move> possibleMoves = sortMoves(getPossibleMoves(controller), controller.chessBoard);

        for (Move move : possibleMoves) {
            logger.info("Evaluating move: " + move.getSource(controller.chessBoard).row + "," + move.getSource(controller.chessBoard).column + " to " + move.getTarget(controller.chessBoard).row + "," + move.getTarget(controller.chessBoard).column);
            int eval = evaluateMove(controller, move);
            if (eval > maxEval) {
                maxEval = eval;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private int evaluateMove(ChessController controller, Move move) {
        ChessController clonedController = cloneAndApplyMove(controller, move);

        return alphaBeta(clonedController, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
    }

    private ChessController cloneAndApplyMove(ChessController controller, Move move) {
        ChessController clonedController = SerializationUtil.deepClone(controller);

        clonedController.chessBoard.MoveFigure(move.getSource(clonedController.chessBoard), move.getTarget(clonedController.chessBoard));
        clonedController.currentTurn = (clonedController.currentTurn == FigureColor.WHITE) ? FigureColor.BLACK : FigureColor.WHITE;
        return clonedController;
    }

    private int alphaBeta(ChessController controller, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        if (depth == 0) {
            if (maxQuiescenceSearchDepth > 0) {
                return quiescenceSearch(controller, alpha, beta, isMaximizingPlayer, maxQuiescenceSearchDepth);
            } else {
                return evaluateBoard.evaluateBoard(controller, logger);
            }
        }

        List<Move> possibleMoves = sortMoves(getPossibleMoves(controller), controller.chessBoard);
        if (possibleMoves.isEmpty()) {
            return evaluateBoard.evaluateBoard(controller, logger);
        }

        if (isMaximizingPlayer) {
            return getMaximizingEval(controller, depth, alpha, beta, possibleMoves);
        } else {
            return getMinimizingEval(controller, depth, alpha, beta, possibleMoves);
        }
    }

    private int getMinimizingEval(ChessController controller, int depth, int alpha, int beta, List<Move> possibleMoves) {
        int minEval = Integer.MAX_VALUE;

        for (Move move : possibleMoves) {
            logger.info("Evaluating subMove: " + move.getSource(controller.chessBoard).row + "," + move.getSource(controller.chessBoard).column + " to " + move.getTarget(controller.chessBoard).row + "," + move.getTarget(controller.chessBoard).column);
            ChessController clonedController = SerializationUtil.deepClone(controller);
            clonedController.chessBoard.MoveFigure(move.getSource(clonedController.chessBoard), move.getTarget(clonedController.chessBoard));
            clonedController.currentTurn = FigureColor.WHITE;

            int eval = alphaBeta(clonedController, depth - 1, alpha, beta, true);
            minEval = Math.min(minEval, eval);
            beta = Math.min(beta, eval);

            if (beta <= alpha) {
                break;
            }
        }
        return minEval;
    }

    private int getMaximizingEval(ChessController controller, int depth, int alpha, int beta, List<Move> possibleMoves) {
        int maxEval = Integer.MIN_VALUE;

        for (Move move : possibleMoves) {
            logger.info("Evaluating subMove: " + move.getSource(controller.chessBoard).row + "," + move.getSource(controller.chessBoard).column + " to " + move.getTarget(controller.chessBoard).row + "," + move.getTarget(controller.chessBoard).column);
            ChessController clonedController = SerializationUtil.deepClone(controller);
            clonedController.chessBoard.MoveFigure(move.getSource(clonedController.chessBoard), move.getTarget(clonedController.chessBoard));
            clonedController.currentTurn = FigureColor.BLACK;

            int eval = alphaBeta(clonedController, depth - 1, alpha, beta, false);
            maxEval = Math.max(maxEval, eval);
            alpha = Math.max(alpha, eval);

            if (beta <= alpha) {
                break;
            }
        }
        return maxEval;
    }

    private List<Move> getPossibleMoves(ChessController controller) {
        List<Move> moves = new ArrayList<>();
        for (Field field : controller.chessBoard.getFields()) {
            if (field.figure != null && field.figure.figureColor == controller.currentTurn) {
                for (Field target : field.figure.getPossibleMoves(controller.chessBoard)) {
                    moves.add(new Move(field, target));
                }
            }
        }
        return moves;
    }

    private List<Move> sortMoves(List<Move> moves, ChessBoard chessBoard) {
        List<Move> sortedMoves = new ArrayList<>();
        List<Move> remainingMoves = new ArrayList<>();

        for (Move move : moves) {
            if (move.getTarget(chessBoard).figure != null) {
                sortedMoves.add(move);
            } else {
                remainingMoves.add(move);
            }
        }

        sortedMoves.addAll(remainingMoves);
        return sortedMoves;
    }

    /**
     * Quiescence search to avoid the horizon effect
     * <p>Expands depth until a move is quite</p>
     * @param controller
     * @param alpha
     * @param beta
     * @param isMaximizingPlayer
     * @return score of the best evaluation
     */
    private int quiescenceSearch(ChessController controller, int alpha, int beta, boolean isMaximizingPlayer, int depth) {
        if (depth == 0) {
            return evaluateBoard.evaluateBoard(controller, logger);
        }
        int standPat = evaluateBoard.evaluateBoard(controller, logger);

        if (isMaximizingPlayer) {
            if (standPat >= beta) return beta;
            alpha = Math.max(alpha, standPat);
        } else {
            if (standPat <= alpha) return alpha;
            beta = Math.min(beta, standPat);
        }

        List<Move> noisyMoves = getNoisyMoves(controller);

        for (Move move : noisyMoves) {
            ChessController cloned = SerializationUtil.deepClone(controller);
            cloned.chessBoard.MoveFigure(move.getSource(cloned.chessBoard), move.getTarget(cloned.chessBoard));
            cloned.currentTurn = (cloned.currentTurn == FigureColor.WHITE) ? FigureColor.BLACK : FigureColor.WHITE;

            int score = quiescenceSearch(cloned, alpha, beta, !isMaximizingPlayer, depth - 1);

            if (isMaximizingPlayer) {
                if (score >= beta) return beta;
                alpha = Math.max(alpha, score);
            } else {
                if (score <= alpha) return alpha;
                beta = Math.min(beta, score);
            }
        }
        return isMaximizingPlayer ? alpha : beta;
    }


    private List<Move> getNoisyMoves(ChessController controller) {
        List<Move> allMoves = getPossibleMoves(controller);
        List<Move> noisyMoves = new ArrayList<>();

        for (Move move : allMoves) {
            Field target = move.getTarget(controller.chessBoard);
            if (target.figure != null && target.figure.figureColor != controller.currentTurn && target.figure.value > 1) {
                noisyMoves.add(move);
            } else if (moveResultsInCheck(controller, move)) {
                noisyMoves.add(move);
            }
        }
        return noisyMoves;
    }

    private boolean moveResultsInCheck(ChessController controller, Move move) {
        ChessController clonedController = SerializationUtil.deepClone(controller);
        clonedController.chessBoard.MoveFigure(move.getSource(clonedController.chessBoard), move.getTarget(clonedController.chessBoard));
        clonedController.currentTurn = (controller.currentTurn == FigureColor.WHITE)
                ? FigureColor.BLACK
                : FigureColor.WHITE;

        return clonedController.getCheckMoveHandler().checkmateHandler.isMate(null) > 0;
    }
}
