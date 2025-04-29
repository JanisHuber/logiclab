package ch.janishuber.logiclab.chess.domain.util;


import ch.janishuber.logiclab.chess.domain.board.ChessBoard;
import ch.janishuber.logiclab.chess.domain.board.Field;
import ch.janishuber.logiclab.chess.domain.enums.FigureColor;
import ch.janishuber.logiclab.chess.domain.figures.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Rook.class, name = "Rook"),
        @JsonSubTypes.Type(value = Knight.class, name = "Knight"),
        @JsonSubTypes.Type(value = Bishop.class, name = "Bishop"),
        @JsonSubTypes.Type(value = Queen.class, name = "Queen"),
        @JsonSubTypes.Type(value = King.class, name = "King"),
        @JsonSubTypes.Type(value = Pawn.class, name = "Pawn")
})
public abstract class ChessFigure implements Serializable {
    private static final long serialVersionUID = 1L;

    public FigureColor figureColor;
    public Field position;
    public int value;

    public abstract List<Field> getPossibleMoves(ChessBoard chessBoard);
    @JsonIgnore
    public String getClassName() {
        return this.getClass().getSimpleName();
    }
}