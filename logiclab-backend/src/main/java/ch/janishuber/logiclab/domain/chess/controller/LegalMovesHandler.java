package ch.janishuber.logiclab.domain.chess.controller;


import ch.janishuber.logiclab.domain.chess.board.ChessBoard;
import ch.janishuber.logiclab.domain.chess.board.Field;
import ch.janishuber.logiclab.domain.chess.enums.FigureColor;
import ch.janishuber.logiclab.domain.chess.util.ChessFigure;
import ch.janishuber.logiclab.domain.chess.util.LegalMovesInCheckHelper;
import ch.janishuber.logiclab.domain.chess.util.LegalMovesOutOfCheckHelper;

import java.io.Serializable;
import java.util.List;

public class LegalMovesHandler implements Serializable {
    private static final long serialVersionUID = 1L;
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
}
