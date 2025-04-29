package ch.janishuber.logiclab.chess.domain.board;


import ch.janishuber.logiclab.chess.domain.util.ChessFigure;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Field implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty
    public int column;
    @JsonProperty
    public String row;
    @JsonProperty
    public ChessFigure figure;


    public int getColumn() { //Spalte
        return column;
    }
    public void setColumn(int column) { //Zeile
        this.column = column;
    }
    public String getRow() {
        return row;
    }
    @JsonIgnore
    public int getRowInt() {
        return (char)(row.charAt(0) - 64);
    }
    public void setRow(String row) {
        this.row = row;
    }
    public ChessFigure getFigure() {
        return figure;
    }
    public void setFigure(ChessFigure figure) {
        this.figure = figure;
    }
}
