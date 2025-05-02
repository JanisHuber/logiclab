package ch.janishuber.logiclab.chess.domain.bot;

import ch.janishuber.logiclab.chess.domain.board.ChessBoard;
import ch.janishuber.logiclab.chess.domain.board.Field;
import ch.janishuber.logiclab.chess.domain.controller.CheckMoveHandler;
import ch.janishuber.logiclab.chess.domain.enums.FigureColor;
import ch.janishuber.logiclab.chess.domain.evaluate.evaluateBoard;
import ch.janishuber.logiclab.chess.domain.util.ChessFigure;
import ch.janishuber.logiclab.chess.domain.util.Move;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class ChessBot {
    private final int depth;
    private final int maxQuiescenceSearchDepth;

    private final List<SimulatedMove> moveHistory = new ArrayList<>();
    private FigureColor currentTurn;

    public ChessBot(int depth, int maxQuiescenceSearchDepth) {
        this.depth = depth;
        this.maxQuiescenceSearchDepth = maxQuiescenceSearchDepth;
    }

    public Move getBestMove(ChessBoard chessBoard, FigureColor currentTurn, FigureColor botColor, String MoveHistoryGame) {
        Move bestMove = null;
        int bestEval = (botColor == FigureColor.WHITE) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        this.currentTurn = currentTurn;

        List<Move> possibleMoves = sortMoves(getAllPossibleCheckedMoves(chessBoard), chessBoard);

        for (Move move : possibleMoves) {
            int eval = evaluateMove(chessBoard, botColor, move);
            if ((botColor == FigureColor.WHITE && eval > bestEval) || (botColor == FigureColor.BLACK && eval < bestEval)) {
                bestEval = eval;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private int evaluateMove(ChessBoard chessBoard, FigureColor botColor, Move move) {
        applyMove(chessBoard, move);

        return alphaBeta(chessBoard, botColor, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
    }

    private int alphaBeta(ChessBoard chessBoard, FigureColor botColor, int depth, int alpha, int beta,
            boolean isMaximizingPlayer) {
        if (depth == 0) {
            if (maxQuiescenceSearchDepth > 0) {
                int result = quiescenceSearch(chessBoard, botColor, alpha, beta, isMaximizingPlayer,
                        maxQuiescenceSearchDepth);
                undoMoves(chessBoard, moveHistory.size());
                return result;
            } else {
                int result = evaluateBoard.evaluateBoard(chessBoard, this.currentTurn, botColor);
                undoMoves(chessBoard, moveHistory.size());
                return result;
            }
        }

        List<Move> possibleMoves = sortMoves(getAllPossibleCheckedMoves(chessBoard), chessBoard);
        if (possibleMoves.isEmpty()) {
            return evaluateBoard.evaluateBoard(chessBoard, this.currentTurn, botColor);
        }

        if (isMaximizingPlayer) {
            return getMaximizingEval(chessBoard, botColor, depth, alpha, beta, possibleMoves);
        } else {
            return getMinimizingEval(chessBoard, botColor, depth, alpha, beta, possibleMoves);
        }
    }

    private int getMinimizingEval(ChessBoard chessBoard, FigureColor botColor, int depth, int alpha, int beta,
            List<Move> possibleMoves) {
        int minEval = Integer.MAX_VALUE;

        for (Move move : possibleMoves) {
            applyMove(chessBoard, move);

            int eval = alphaBeta(chessBoard, botColor, depth - 1, alpha, beta, true);
            minEval = Math.min(minEval, eval);
            beta = Math.min(beta, eval);

            if (beta <= alpha) {
                break;
            }
        }
        return minEval;
    }

    private int getMaximizingEval(ChessBoard chessBoard, FigureColor botColor, int depth, int alpha, int beta,
            List<Move> possibleMoves) {
        int maxEval = Integer.MIN_VALUE;

        for (Move move : possibleMoves) {
            applyMove(chessBoard, move);

            int eval = alphaBeta(chessBoard, botColor, depth - 1, alpha, beta, false);
            maxEval = Math.max(maxEval, eval);
            alpha = Math.max(alpha, eval);

            if (beta <= alpha) {
                break;
            }
        }
        return maxEval;
    }

    private List<Move> getAllPossibleCheckedMoves(ChessBoard chessBoard) {
        List<Move> moves = new ArrayList<>();
        CheckMoveHandler checkMoveHandler = new CheckMoveHandler(chessBoard, this.currentTurn);
        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() != null && field.getFigure().figureColor == this.currentTurn) {
                if (checkMoveHandler.getCheckedMove(field.getFigure()) == null) {
                    continue;
                }
                for (Field target : checkMoveHandler.getCheckedMove(field.getFigure())) {
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
            if (move.getTarget(chessBoard).getFigure() != null) {
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
     * <p>
     * Expands depth until a move is quite
     * </p>
     * 
     * @param controller
     * @param alpha
     * @param beta
     * @param isMaximizingPlayer
     * @return score of the best evaluation
     */
    private int quiescenceSearch(ChessBoard chessBoard, FigureColor botColor, int alpha, int beta,
            boolean isMaximizingPlayer, int depth) {
        if (depth == 0) {
            System.out.println("Quiescence search depth reached");
            return evaluateBoard.evaluateBoard(chessBoard, this.currentTurn, botColor);
        }
        int standPat = evaluateBoard.evaluateBoard(chessBoard, this.currentTurn, botColor);

        if (isMaximizingPlayer) {
            if (standPat >= beta)
                return beta;
            alpha = Math.max(alpha, standPat);
        } else {
            if (standPat <= alpha)
                return alpha;
            beta = Math.min(beta, standPat);
        }

        List<Move> noisyMoves = getNoisyMoves(chessBoard);
        if (noisyMoves.isEmpty()) {
            return standPat;
        }

        for (Move move : noisyMoves) {
            applyMove(chessBoard, move);

            int score = quiescenceSearch(chessBoard, botColor, alpha, beta, !isMaximizingPlayer, depth - 1);

            if (isMaximizingPlayer) {
                if (score >= beta)
                    return beta;
                alpha = Math.max(alpha, score);
            } else {
                if (score <= alpha)
                    return alpha;
                beta = Math.min(beta, score);
            }
        }
        return isMaximizingPlayer ? alpha : beta;
    }

    private List<Move> getNoisyMoves(ChessBoard chessBoard) {
        List<Move> allMoves = getAllPossibleCheckedMoves(chessBoard);
        List<Move> noisyMoves = new ArrayList<>();
        if (allMoves.isEmpty()) {
            return noisyMoves;
        }

        for (Move move : allMoves) {
            Field target = move.getTarget(chessBoard);
            if (target.getFigure() != null && target.getFigure().figureColor != this.currentTurn
                    && target.getFigure().value > 1) {
                noisyMoves.add(move);
            } else if (moveResultsInCheck(chessBoard, move)) {
                noisyMoves.add(move);
            }
        }
        return noisyMoves;
    }

    private boolean moveResultsInCheck(ChessBoard chessBoard, Move move) {
        applyMove(chessBoard, move);
        CheckMoveHandler checkMoveHandler = new CheckMoveHandler(chessBoard, this.currentTurn);
        boolean resultsInCheck = checkMoveHandler.checkmateHandler.isMate(null) > 0;
        undoMoves(chessBoard, 1);
        return resultsInCheck;
    }

    private void applyMove(ChessBoard chessBoard, Move move) {
        Field source = move.getSource(chessBoard);
        Field target = move.getTarget(chessBoard);

        ChessFigure capturedFigure = target.getFigure();

        if (source.getFigure() != null) {
            source.getFigure().position = target;
        }
        if (target.getFigure() != null) {
            target.getFigure().position = null;
        }

        target.setFigure(source.getFigure());
        source.setFigure(null);
        this.currentTurn = (this.currentTurn == FigureColor.WHITE) ? FigureColor.BLACK : FigureColor.WHITE;

        SimulatedMove simMove = new SimulatedMove(source, target, capturedFigure);
        moveHistory.add(simMove);
    }

    private void undoMoves(ChessBoard chessBoard, int depth) {
        int movesToUndo = Math.min(depth, moveHistory.size());

        for (int i = 0; i < movesToUndo; i++) {
            SimulatedMove simMove = (SimulatedMove) moveHistory.remove(moveHistory.size() - 1);
            Field source = simMove.source();
            Field target = simMove.target();
            ChessFigure capturedFigure = simMove.figure();

            ChessFigure movedFigure = target.getFigure();
            source.setFigure(movedFigure);
            if (movedFigure != null) {
                movedFigure.position = source;
            }

            target.setFigure(capturedFigure);
            if (capturedFigure != null) {
                capturedFigure.position = target;
            }

            this.currentTurn = (this.currentTurn == FigureColor.WHITE) ? FigureColor.BLACK : FigureColor.WHITE;
        }
    }
}
