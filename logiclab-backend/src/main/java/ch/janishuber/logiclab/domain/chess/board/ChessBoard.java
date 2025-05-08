package ch.janishuber.logiclab.domain.chess.board;

import java.io.Serializable;
import java.util.List;

public class ChessBoard implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Field> fields;

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public static ChessBoard ofExisting(String fieldsJson) {
        return BoardMapperHelper.mapChessBoard(fieldsJson);
    }

    public Field getField(String column, int row) {
        for (Field field : fields) {
            if (field.getColumn().equals(column) && field.getRow() == row) {
                return field;
            }
        }
        return null;
    }

    public void MoveFigure(Field source, Field target) {
        if (source.getFigure() == null) {
            return;
        }

        if (target.getFigure() != null && target.getFigure().figureColor != source.getFigure().figureColor) {
            target.getFigure().position = null;
            target.setFigure(null);
        } else if (target.getFigure() != null && target.getFigure().figureColor == source.getFigure().figureColor) {
            return;
        }

        source.getFigure().position = target;
        target.setFigure(source.getFigure());
        source.setFigure(null);
    }
}
