package ch.janishuber.logiclab.domain.chess.bot;

import ch.janishuber.logiclab.domain.chess.board.ChessBoard;
import ch.janishuber.logiclab.domain.chess.board.Field;
import ch.janishuber.logiclab.domain.chess.controller.LegalMovesHandler;
import ch.janishuber.logiclab.domain.chess.enums.FigureColor;
import ch.janishuber.logiclab.domain.chess.evaluate.OpeningBook;
import ch.janishuber.logiclab.domain.chess.evaluate.evaluateBoard;
import ch.janishuber.logiclab.domain.chess.util.ChessFigure;
import ch.janishuber.logiclab.domain.chess.util.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    public Move getBestMove(ChessBoard chessBoard, FigureColor currentTurn, FigureColor botColor, String moveHistoryGame) {
        List<String> moveHistoryList = new ArrayList<>();
        for (String move : moveHistoryGame.split(",")) {
            moveHistoryList.add(move);
        }
        if (OpeningBook.isInOpening(chessBoard, Collections.singletonList(moveHistoryGame))) {
            Optional<String> nextBookMove = OpeningBook.getNextMove(Collections.singletonList(moveHistoryGame));
            if (nextBookMove.isPresent()) {
                String move = nextBookMove.get();
                Optional<Move> bookMove = convertFENtoMove(move, chessBoard);
                if (bookMove.isPresent()) {
                    System.out.println("BookMove: " + bookMove.get());
                    return bookMove.get();
                }
            }
        }

        Move bestMove = null;
        int bestEval = (botColor == FigureColor.WHITE) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        List<Move> possibleMoves = sortMoves(getAllPossibleCheckedMoves(chessBoard, currentTurn), chessBoard, moveHistoryGame);
        for (Move move : possibleMoves) {
            int eval = evaluateMove(chessBoard, botColor, move, moveHistoryGame, currentTurn);
            if ((botColor == FigureColor.WHITE && eval > bestEval) || (botColor == FigureColor.BLACK && eval < bestEval)) {
                System.out.println("Old eval: " + bestEval + " New eval: " + eval);
                bestEval = eval;
                bestMove = move;
                System.out.println("Best move: " + bestMove.source().getColumn() + bestMove.source().getRow() + "To: " + bestMove.target().getColumn() + bestMove.target().getRow() + " with evaluation: " + bestEval);
            }
        }
        return bestMove;
    }

    private int evaluateMove(ChessBoard chessBoard, FigureColor botColor, Move move, String moveHistoryGame, FigureColor currentTurn) {
        applyMove(chessBoard, move);
        FigureColor nextTurn = (currentTurn == FigureColor.WHITE) ? FigureColor.BLACK : FigureColor.WHITE;
        boolean isMaximizingPlayer = (nextTurn == botColor);
        int eval = alphaBeta(chessBoard, botColor, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, isMaximizingPlayer, moveHistoryGame, nextTurn);
        undoMoves(chessBoard, 1);
        return eval;
    }

    private int alphaBeta(ChessBoard chessBoard, FigureColor botColor, int depth, int alpha, int beta, boolean isMaximizingPlayer, String moveHistoryGame, FigureColor currentTurn) {
        if (depth == 0) {
            if (maxQuiescenceSearchDepth > 0) {
                int result = quiescenceSearch(chessBoard, botColor, alpha, beta, isMaximizingPlayer, maxQuiescenceSearchDepth, moveHistoryGame, currentTurn);
                return result;
            } else {
                int result = evaluateBoard.evaluateBoard(chessBoard, currentTurn, botColor);
                return result;
            }
        }

        List<Move> possibleMoves = sortMoves(getAllPossibleCheckedMoves(chessBoard, currentTurn), chessBoard, moveHistoryGame);
        if (possibleMoves.isEmpty()) {
            return evaluateBoard.evaluateBoard(chessBoard, currentTurn, botColor);
        }

        if (isMaximizingPlayer) {
            return getMaximizingEval(chessBoard, botColor, depth, alpha, beta, possibleMoves, moveHistoryGame, currentTurn);
        } else {
            return getMinimizingEval(chessBoard, botColor, depth, alpha, beta, possibleMoves, moveHistoryGame, currentTurn);
        }
    }

    private int getMinimizingEval(ChessBoard chessBoard, FigureColor botColor, int depth, int alpha, int beta, List<Move> possibleMoves, String moveHistoryGame, FigureColor currentTurn) {
        int minEval = Integer.MAX_VALUE;

        for (Move move : possibleMoves) {
            applyMove(chessBoard, move);
            FigureColor nextTurn = (currentTurn == FigureColor.WHITE) ? FigureColor.BLACK : FigureColor.WHITE;
            boolean isMaximizingPlayer = (nextTurn == botColor);
            int eval = alphaBeta(chessBoard, botColor, depth - 1, alpha, beta, isMaximizingPlayer, moveHistoryGame, nextTurn);
            undoMoves(chessBoard, 1);
            minEval = Math.min(minEval, eval);
            beta = Math.min(beta, eval);

            if (beta <= alpha) {
                break;
            }
        }
        return minEval;
    }

    private int getMaximizingEval(ChessBoard chessBoard, FigureColor botColor, int depth, int alpha, int beta, List<Move> possibleMoves, String moveHistoryGame, FigureColor currentTurn) {
        int maxEval = Integer.MIN_VALUE;

        for (Move move : possibleMoves) {
            applyMove(chessBoard, move);
            FigureColor nextTurn = (currentTurn == FigureColor.WHITE) ? FigureColor.BLACK : FigureColor.WHITE;
            boolean isMaximizingPlayer = (nextTurn == botColor);
            int eval = alphaBeta(chessBoard, botColor, depth - 1, alpha, beta, isMaximizingPlayer, moveHistoryGame, nextTurn);
            undoMoves(chessBoard, 1);
            maxEval = Math.max(maxEval, eval);
            alpha = Math.max(alpha, eval);

            if (beta <= alpha) {
                break;
            }
        }
        return maxEval;
    }

    private List<Move> getAllPossibleCheckedMoves(ChessBoard chessBoard, FigureColor currentTurn) {
        List<Move> moves = new ArrayList<>();
        LegalMovesHandler legalMovesHandler = new LegalMovesHandler(chessBoard, currentTurn);
        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() != null && field.getFigure().figureColor == currentTurn) {
                if (legalMovesHandler.getLegalMoves(field.getFigure()) == null) {
                    continue;
                }
                for (Field target : legalMovesHandler.getLegalMoves(field.getFigure())) {
                    moves.add(new Move(field, target));
                }
            }
        }
        return moves;
    }

    private List<Move> sortMoves(List<Move> moves, ChessBoard chessBoard, String moveHistoryGame) {
        List<Move> sortedMoves = new ArrayList<>();
        List<Move> remainingMoves = new ArrayList<>();

        for (Move move : moves) {
            if (move.getTarget(chessBoard).getFigure() != null) {
                if (move.getTarget(chessBoard).getFigure().value > 1) {
                    sortedMoves.add(0, move);
                } else {
                    sortedMoves.add(move);
                }
            } else {
                remainingMoves.add(move);
            }
        }
        sortedMoves.addAll(remainingMoves);
        return sortedMoves;
        /*if (moveHistoryGame == null || moveHistoryGame.isEmpty()) {
            return sortedMoves;
        }
        String[] historyMoves = moveHistoryGame.split(",");
        for (String move : historyMoves) {
            Optional<Move> historyMove = convertFENtoMove(move, chessBoard);
            if (historyMove.isPresent()) {
                if (sortedMoves.contains(historyMove.get())) {
                    sortedMoves.remove(historyMove);
                }
            }
        }

        return sortedMoves;*/
    }

    /**
     * Quiescence search to avoid the horizon effect
     * <p>
     * Expands depth until a moveOnChessboard is quite
     * </p>
     *
     * @param controller
     * @param alpha
     * @param beta
     * @param isMaximizingPlayer
     * @return score of the best evaluation
     */
    private int quiescenceSearch(ChessBoard chessBoard, FigureColor botColor, int alpha, int beta, boolean isMaximizingPlayer, int depth, String moveHistoryGame, FigureColor currentTurn) {
        if (depth == 0) {
            System.out.println("Quiescence search depth reached");
            return evaluateBoard.evaluateBoard(chessBoard, currentTurn, botColor);
        }
        int standPat = evaluateBoard.evaluateBoard(chessBoard, currentTurn, botColor);

        if (isMaximizingPlayer) {
            if (standPat >= beta) return beta;
            alpha = Math.max(alpha, standPat);
        } else {
            if (standPat <= alpha) return alpha;
            beta = Math.min(beta, standPat);
        }

        List<Move> noisyMoves = getNoisyMoves(chessBoard, currentTurn);
        if (noisyMoves.isEmpty()) {
            return standPat;
        }

        for (Move move : noisyMoves) {
            applyMove(chessBoard, move);

            int score = quiescenceSearch(chessBoard, botColor, alpha, beta, !isMaximizingPlayer, depth - 1, moveHistoryGame, currentTurn);
            undoMoves(chessBoard, 1);

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

    private List<Move> getNoisyMoves(ChessBoard chessBoard, FigureColor currentTurn) {
        List<Move> allMoves = getAllPossibleCheckedMoves(chessBoard, currentTurn);
        List<Move> noisyMoves = new ArrayList<>();
        if (allMoves.isEmpty()) {
            return noisyMoves;
        }

        for (Move move : allMoves) {
            Field target = move.getTarget(chessBoard);
            if (target.getFigure() != null && target.getFigure().figureColor != currentTurn && target.getFigure().value > 1) {
                noisyMoves.add(move);
            } else if (moveResultsInCheck(chessBoard, move, currentTurn)) {
                noisyMoves.add(move);
            }
        }
        return noisyMoves;
    }

    private boolean moveResultsInCheck(ChessBoard chessBoard, Move move, FigureColor currentTurn) {
        applyMove(chessBoard, move);
        LegalMovesHandler legalMovesHandler = new LegalMovesHandler(chessBoard, currentTurn);
        boolean resultsInCheck = legalMovesHandler.checkmateHandler.isMate(null) > 0;
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

        }
    }

    private Optional<Move> convertFENtoMove(String fen, ChessBoard chessBoard) {
        String[] moveParts = fen.split("-");
        if (moveParts.length == 2) {
            String sourceColumn = moveParts[0].substring(0, 1);
            int sourceRow = Integer.parseInt(moveParts[0].substring(1));

            String targetColumn = moveParts[1].substring(0, 1);
            int targetRow = Integer.parseInt(moveParts[1].substring(1));

            Field sourceField = chessBoard.getField(sourceColumn, sourceRow);
            Field targetField = chessBoard.getField(targetColumn, targetRow);
            return Optional.of(new Move(sourceField, targetField));
        } else {
            return Optional.empty();
        }
    }
}
