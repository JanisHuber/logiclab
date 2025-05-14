package ch.janishuber.logiclab.domain.chess.controller;


import ch.janishuber.logiclab.domain.chess.board.ChessBoard;
import ch.janishuber.logiclab.domain.chess.board.Field;
import ch.janishuber.logiclab.domain.chess.enums.FigureColor;
import ch.janishuber.logiclab.domain.chess.util.ChessFigure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("t")
public class CheckmateHandler {

    private final ChessBoard chessBoard;
    private final FigureColor currentTurn;

    private final List<Field> possibleCaptureSources = new ArrayList<>();
    private final List<Field> possibleBlockSources = new ArrayList<>();

    public CheckmateHandler(ChessBoard chessBoard, FigureColor currentTurn) {
        this.chessBoard = chessBoard;
        this.currentTurn = currentTurn;
    }

    public List<Field> getPossibleCaptureSources() {
        return possibleCaptureSources;
    }

    public List<Field> getPossibleBlockSources() {
        return possibleBlockSources;
    }

    /**
     * Get all possible mates against the king of the currentTurn.
     * @param kingField The field of the king. If null, it will search for the king's field.
     * @return List of possible mates.
     */
    private List<Field> getPossiblesMates(Field kingField) {
        List<Field> possibleMates = new ArrayList<>();
        List<Field> currentCheckingFields = new ArrayList<>();
        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() == null) {
                continue;
            }
            String figureType = field.getFigure().getClassName();
            if (figureType.equals("King") && field.getFigure().figureColor == currentTurn) {
                if (kingField == null) {
                    kingField = field;
                }
            }
            else if (field.getFigure().figureColor != currentTurn) {
                currentCheckingFields.add(field);
            }
        }
        for (Field field : currentCheckingFields) {
            for (Field figureField : field.getFigure().getPossibleMoves(chessBoard)) {
                if (figureField == kingField) {
                    possibleMates.add(field);
                }
            }
        }
        return possibleMates;
    }

    /**
     * Check if the king is in checkmate.
     * @param kingField The field of the king. If null, it will search for the king's field.
     * @return size of possible mates.
     */
    public int isMate(Field kingField) {
        if (kingField == null) {
            for (Field field : chessBoard.getFields()) {
                if (field.getFigure() == null) {
                    continue;
                }
                String figureType = field.getFigure().getClassName();
                if (figureType.equals("King") && field.getFigure().figureColor == currentTurn) {
                    kingField = field;
                }
            }
        }
        List<Field> possibleMates = getPossiblesMates(kingField);
        return possibleMates.size();
    }

    /**
     * Checks for possible captures to escape mate.
     * @return List of possible capturesTargetFields
     * @saves the @possibleCaptureSources
     */
    public List<Field> canCapture() {
        possibleCaptureSources.clear();
        List<Field> possibleCaptures = new ArrayList<>();
        List<Field> possibleMates = getPossiblesMates(null);
        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() == null) {
                continue;
            }
            if (field.getFigure().figureColor == currentTurn) {
                for (Field mate : possibleMates) {
                    for (Field figureField : field.getFigure().getPossibleMoves(chessBoard)) {
                        if (figureField == mate) {
                            ChessFigure originalFigure = figureField.getFigure();
                            Field originalPosition = field.getFigure().position;

                            boolean isOutOfCheck = isMate(null) == 0;

                            figureField.setFigure(originalFigure);
                            field.getFigure().position = originalPosition;
                            if (isOutOfCheck) {
                                possibleCaptures.add(figureField);
                                possibleCaptureSources.add(field);
                            }
                        }
                    }
                }
            }
        }
        return possibleCaptures;
    }

    /**
     * Checks for possible blocks to escape mate.
     * @return List of possible blocksTargetFields
     * @saves: the possibleBlockSources
     */
    public List<Field> canBlock() {
        possibleBlockSources.clear();
        List<Field> blocks = new ArrayList<>();
        ChessFigure originalFigure;
        boolean isOutOfCheck;

        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() == null) {
                continue;
            }
            String figureType = field.getFigure().getClassName();
            if (figureType.equals("King") && field.getFigure().figureColor == currentTurn) {
                continue;
            }
            if (field.getFigure().figureColor != currentTurn) {
                continue;
            }
            for (Field possibleMove : field.getFigure().getPossibleMoves(chessBoard)) {
                isOutOfCheck = false;
                if (possibleMove.getFigure() != null) {
                    originalFigure = possibleMove.getFigure();
                } else {
                    originalFigure = null;
                }
                possibleMove.setFigure(field.getFigure());
                possibleMove.getFigure().position = possibleMove;

                isOutOfCheck = isMate(null) == 0;

                field.getFigure().position = field;
                possibleMove.setFigure(originalFigure);
                if (isOutOfCheck) {
                    blocks.add(possibleMove);
                    possibleBlockSources.add(field);
                }
            }
        }
        return blocks;
    }

    /**
     * Checks for possible escapes to escape mate.
     * @return List of possible escapesTargetFields
     */
    public List<Field> canEscape() {
        List<Field> possibleEscapes;
        List<Field> escapes = new ArrayList<>();
        Field kingField = null;
        boolean isOutOfCheck;

        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() == null) {
                continue;
            }
            String figureType = field.getFigure().getClassName();
            if (figureType.equals("King") && field.getFigure().figureColor == currentTurn) {
                kingField = field;
            }
        }
        possibleEscapes = kingField.getFigure().getPossibleMoves(chessBoard);
        for (Field escapingField : possibleEscapes) {
            isOutOfCheck = false;
            ChessFigure king = kingField.getFigure();
            ChessFigure captured = escapingField.getFigure();

            escapingField.setFigure(king);
            kingField.setFigure(null);
            king.position = escapingField;

            isOutOfCheck = isMate(escapingField) == 0;

            king.position = kingField;
            kingField.setFigure(king);
            escapingField.setFigure(captured);

            if (isOutOfCheck) {
                escapes.add(escapingField);
            }
        }
        return escapes;
    }
}
