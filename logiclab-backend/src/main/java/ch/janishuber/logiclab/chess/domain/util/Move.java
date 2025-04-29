package ch.janishuber.logiclab.chess.domain.util;


import ch.janishuber.logiclab.chess.domain.board.ChessBoard;
import ch.janishuber.logiclab.chess.domain.board.Field;

public class Move {
    private final Field source;
    private final Field target;

    public Move(Field source, Field target) {
        this.source = source;
        this.target = target;
    }

    public Field getSource(ChessBoard board) {
        return board.getField(source.row, source.column);
    }

    public Field getTarget(ChessBoard board) {
        return board.getField(target.row, target.column);
    }
}
