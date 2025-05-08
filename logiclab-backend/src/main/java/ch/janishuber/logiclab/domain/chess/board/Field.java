package ch.janishuber.logiclab.domain.chess.board;


import ch.janishuber.logiclab.domain.chess.util.ChessFigure;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Field implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty
    private String column;
    @JsonProperty
    private int row;
    @JsonProperty
    private ChessFigure figure;

    public Field() {}

    public Field(String column, int row) {
        this.column = column;
        this.row = row;
    }

    public String getColumn() {
        return column;
    }
    public void setColumn(String column) {
        this.column = column;
    }
    public int getRow() {
        return row;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public ChessFigure getFigure() {
        return figure;
    }
    public void setFigure(ChessFigure figure) {
        this.figure = figure;
    }
}
