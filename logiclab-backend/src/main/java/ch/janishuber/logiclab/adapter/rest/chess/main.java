package ch.janishuber.logiclab.adapter.rest.chess;

import ch.janishuber.logiclab.domain.chess.board.BoardMapperHelper;
import ch.janishuber.logiclab.domain.chess.board.ChessBoard;
import ch.janishuber.logiclab.domain.chess.enums.FigureColor;
import ch.janishuber.logiclab.domain.chess.evaluate.evaluateBoard;

public class main {
    public static void main(String[] args) {
        String jsonChessBoard = "";

        ChessBoard board = BoardMapperHelper.mapChessBoard(jsonChessBoard);
        System.out.println(evaluateBoard.evaluateBoard(board, FigureColor.WHITE, FigureColor.BLACK));
    }
}
