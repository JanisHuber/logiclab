package ch.janishuber.logiclab.domain.chess.controller;


import ch.janishuber.logiclab.domain.chess.board.ChessBoard;
import ch.janishuber.logiclab.domain.chess.board.Field;
import ch.janishuber.logiclab.domain.chess.enums.FigureColor;
import ch.janishuber.logiclab.domain.chess.util.ChessFigure;
import ch.janishuber.logiclab.domain.chess.util.LegalMovesInCheckHelper;
import ch.janishuber.logiclab.domain.chess.util.LegalMovesOutOfCheckHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LegalMovesHandler {
    private final ChessBoard chessBoard;
    private final FigureColor currentTurn;

    public final CheckmateHandler checkmateHandler;

    public LegalMovesHandler(ChessBoard chessBoard, FigureColor currentTurn) {
        this.chessBoard = chessBoard;
        this.currentTurn = currentTurn;

        this.checkmateHandler = new CheckmateHandler(chessBoard, currentTurn);
    }

    public List<Field> getLegalMoves(ChessFigure figure)
    {
        if (figure.figureColor != currentTurn) {
            return List.of();
        }
        List<Field> figureFields = figure.getPossibleMoves(chessBoard);

        if (checkmateHandler.isMate(null) > 0) {
            List<Field> escapes = checkmateHandler.canEscape();
            List<Field> captures = checkmateHandler.canCapture();
            List<Field> blocks = checkmateHandler.canBlock();

            figureFields = LegalMovesInCheckHelper.resolveLegalMoves(figureFields, captures, blocks, escapes, figure, checkmateHandler.getPossibleCaptureSources(), checkmateHandler.getPossibleBlockSources());
        }

        figureFields = LegalMovesOutOfCheckHelper.filterMovesPreventingCheck(chessBoard, checkmateHandler, figureFields, figure);

        return figureFields;
    }

    public List<Field> getLegalCastlingMoves(ChessFigure figure) {
        List<Field> possibleMoves = new ArrayList<>();
        if (figure.position.getRow() == 1 || figure.position.getRow() == 8 && !figure.hasMoved) {
            Field rightRookField = chessBoard.getField("H", figure.position.getRow());
            Field leftRookField = chessBoard.getField("A", figure.position.getRow());

            if (rightRookField.getFigure() != null && !rightRookField.getFigure().hasMoved) {
                List<Field> possibleMovesForRightRook = rightRookField.getFigure().getPossibleMoves(chessBoard);
                if (possibleMovesForRightRook.contains(chessBoard.getField("F", figure.position.getRow()))) {
                    possibleMoves.add(chessBoard.getField("G", figure.position.getRow()));
                }

            }
            if (leftRookField.getFigure() != null && !leftRookField.getFigure().hasMoved) {
                List<Field> possibleMovesForLeftRook = leftRookField.getFigure().getPossibleMoves(chessBoard);
                if (possibleMovesForLeftRook.contains(chessBoard.getField("D", figure.position.getRow()))) {
                    possibleMoves.add(chessBoard.getField("C", figure.position.getRow()));
                }
            }
        }
        return possibleMoves;
    }
}
