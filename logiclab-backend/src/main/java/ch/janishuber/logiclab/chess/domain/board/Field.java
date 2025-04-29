package org.example.chess.backend.board;
import java.io.Serializable;

import org.example.chess.backend.util.ChessFigure;


public class Field implements Serializable {
    private static final long serialVersionUID = 1L;

    public int column;
    public String row;
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
