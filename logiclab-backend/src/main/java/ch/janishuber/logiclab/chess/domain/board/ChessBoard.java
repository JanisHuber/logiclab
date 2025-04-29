package org.example.chess.backend.board;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

public class ChessBoard implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Field> fields;

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public Field getField(String row, int column) {
        for (Field field : fields) {
            if (field.row.equals(row) && field.column == column) {
                return field;
            }
        }
        return null;
    }

    public void MoveFigure(Field source, Field target) {
        if (source.figure == null) {
            return;
        }

        if (target.figure != null && target.figure.figureColor != source.figure.figureColor) {
            target.figure.position = null;
            target.figure = null;
        } else if (target.figure != null && target.figure.figureColor == source.figure.figureColor) {
            return;
        }

        source.figure.position = target;
        target.figure = source.figure;
        source.figure = null;
    }
    public void forEachField(Consumer<Field> action) {
        for (Field field : fields) {
            action.accept(field);
        }
    }
}
