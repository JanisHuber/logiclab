package ch.janishuber.logiclab.chess.domain.controller;


import ch.janishuber.logiclab.chess.domain.board.ChessBoard;
import ch.janishuber.logiclab.chess.domain.board.Field;
import ch.janishuber.logiclab.chess.domain.enums.FigureColor;
import ch.janishuber.logiclab.chess.domain.util.ChessFigure;
import ch.janishuber.logiclab.chess.domain.util.LegalMovesInCheckHelper;
import ch.janishuber.logiclab.chess.domain.util.LegalMovesOutOfCheckHelper;

import java.io.Serializable;
import java.util.List;

public class CheckMoveHandler implements Serializable {
    private static final long serialVersionUID = 1L;
    private final ChessBoard chessBoard;
    private final FigureColor currentTurn;

    public final CheckmateHandler checkmateHandler;

    public CheckMoveHandler(ChessBoard chessBoard, FigureColor currentTurn) {
        this.chessBoard = chessBoard;
        this.currentTurn = currentTurn;

        this.checkmateHandler = new CheckmateHandler(chessBoard, currentTurn);
    }

    public List<Field> getCheckedMove(ChessFigure figure)
    {
        if (figure.figureColor != currentTurn) {
            return null;
        }
        List<Field> figureFields = figure.getPossibleMoves(chessBoard);
        System.out.println(figureFields);

        if (checkmateHandler.isMate(null) > 0) {
            List<Field> escapes = checkmateHandler.canEscape();
            List<Field> captures = checkmateHandler.canCapture();
            List<Field> blocks = checkmateHandler.canBlock();

            figureFields = LegalMovesInCheckHelper.resolveLegalMoves(figureFields, captures, blocks, escapes, figure, checkmateHandler.getPossibleCaptureSources(), checkmateHandler.getPossibleBlockSources());
        }

        figureFields = LegalMovesOutOfCheckHelper.filterMovesPreventingCheckmate(chessBoard, checkmateHandler, figureFields, figure);

        return figureFields;
    }
}
