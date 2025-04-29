package org.example.chess.backend.controller;

import org.example.chess.backend.board.ChessBoard;
import org.example.chess.backend.util.ChessFigure;
import org.example.chess.backend.board.Field;
import org.example.chess.backend.enums.FigureColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CheckmateHandler implements Serializable {
    private static final long serialVersionUID = 1L;

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
            if (field.figure == null) {
                continue;
            }
            String figureType = field.figure.getClassName();
            if (figureType.equals("King") && field.figure.figureColor == currentTurn) {
                if (kingField == null) {
                    kingField = field;
                }
            }
            else if (field.figure.figureColor != currentTurn) {
                currentCheckingFields.add(field);
            }
        }
        for (Field field : currentCheckingFields) {
            for (Field figureField : field.figure.getPossibleMoves()) {
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
                if (field.figure == null) {
                    continue;
                }
                String figureType = field.figure.getClassName();
                if (figureType.equals("King") && field.figure.figureColor == currentTurn) {
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
            if (field.figure == null) {
                continue;
            }
            if (field.figure.figureColor == currentTurn) {
                for (Field mate : possibleMates) {
                    for (Field figureField : field.figure.getPossibleMoves()) {
                        if (figureField == mate) {
                            ChessFigure originalFigure = figureField.figure;
                            Field originalPosition = field.figure.position;

                            boolean isOutOfCheck = isMate(null) == 0;

                            figureField.figure = originalFigure;
                            field.figure.position = originalPosition;
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
            if (field.figure == null) {
                continue;
            }
            String figureType = field.figure.getClassName();
            if (figureType.equals("King") && field.figure.figureColor == currentTurn) {
                continue;
            }
            if (field.figure.figureColor != currentTurn) {
                continue;
            }
            for (Field possibleMove : field.figure.getPossibleMoves()) {
                isOutOfCheck = false;
                if (possibleMove.figure != null) {
                    originalFigure = possibleMove.figure;
                } else {
                    originalFigure = null;
                }
                possibleMove.figure = field.figure;
                possibleMove.figure.position = possibleMove;

                isOutOfCheck = isMate(null) == 0;

                field.figure.position = field;
                possibleMove.figure = originalFigure;
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
            if (field.figure == null) {
                continue;
            }
            String figureType = field.figure.getClassName();
            if (figureType.equals("King") && field.figure.figureColor == currentTurn) {
                kingField = field;
            }
        }
        possibleEscapes = kingField.figure.getPossibleMoves();
        for (Field escapingField : possibleEscapes) {
            isOutOfCheck = false;
            ChessFigure king = kingField.figure;
            ChessFigure captured = escapingField.figure;

            escapingField.figure = king;
            kingField.figure = null;
            king.position = escapingField;

            isOutOfCheck = isMate(escapingField) == 0;

            king.position = kingField;
            kingField.figure = king;
            escapingField.figure = captured;

            if (isOutOfCheck) {
                escapes.add(escapingField);
            }
        }
        return escapes;
    }
}
