package ch.janishuber.logiclab.chess.domain.board;

import ch.janishuber.logiclab.chess.adapter.rest.dto.FieldDto;
import ch.janishuber.logiclab.chess.adapter.rest.dto.FigureDto;
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

    public static List<FieldDto> convertToDTO(List<Field> fields) {
        List<FieldDto> fieldDTOs = new ArrayList<>();
        for (Field field : fields) {
            FieldDto dto = null;
            if (field.getFigure() != null) {
                dto = new FieldDto(field.getColumn(), field.getRow(), new FigureDto(field.figure.getClassName(), field.figure.figureColor.toString(), field.figure.value));
            } else {
                dto = new FieldDto(field.getColumn(), field.getRow(), null);
            }
            fieldDTOs.add(dto);
        }
        return fieldDTOs;
    }
}
