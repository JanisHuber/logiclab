package org.example.chess.backend.util;

import org.example.chess.backend.board.ChessBoard;
import org.example.chess.backend.board.Field;

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
