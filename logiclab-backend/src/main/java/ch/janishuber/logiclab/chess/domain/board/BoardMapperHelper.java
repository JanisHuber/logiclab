package ch.janishuber.logiclab.chess.domain.board;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class BoardMapperHelper {

    /**
     * Converts a JSON string representation of a chessboard into a ChessBoard object.
     * @param fieldsJson
     */
    public static ChessBoard mapChessBoard(String fieldsJson) {
        ChessBoard mappedChessBoard = new ChessBoard();
        ObjectMapper mapper = new ObjectMapper();
        List<Field> fields = new ArrayList<>();
        try {
            fields = mapper.readValue(fieldsJson,mapper.getTypeFactory().constructCollectionType(List.class, Field.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Field> mappedFields = convertToJava(fields);
        mappedChessBoard.setFields(mappedFields);

        return mappedChessBoard;
    }

    public static List<Field> convertToJson(List<Field> fields) {
        for (Field field : fields) {
            if (field.getFigure() != null) {
                field.getFigure().position = null;
            }
        }
        return fields;
    }

    public static List<Field> convertToJava(List<Field> fields) {
        for (Field field : fields) {
            if (field.getFigure() != null) {
                field.getFigure().position = field;
            }
        }
        return fields;
    }
}
