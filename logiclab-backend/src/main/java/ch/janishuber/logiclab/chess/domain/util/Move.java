package ch.janishuber.logiclab.chess.domain.util;


import ch.janishuber.logiclab.chess.domain.board.ChessBoard;
import ch.janishuber.logiclab.chess.domain.board.Field;

public record Move(Field source, Field target) {

    public Field getSource(ChessBoard board) {
        return board.getField(source.getColumn(), source.getRow());
    }

    public Field getTarget(ChessBoard board) {
        return board.getField(target.getColumn(), target.getRow());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move move)) return false;

        if (!source.equals(move.source)) return false;
        return target.equals(move.target);
    }
}
