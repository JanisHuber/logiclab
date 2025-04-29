package ch.janishuber.logiclab.chess.domain.util;


import ch.janishuber.logiclab.chess.domain.board.Field;

import java.util.ArrayList;
import java.util.List;

public class LegalMovesInCheckHelper {

    public final static String KING_CLASS_NAME = "King";

    /**
     * Filters the list of possible moves for a figure when the king is in check.
     * <p>
     * This method determines whether a given figure can help resolve a check against the king
     * by evaluating if it can escape (in case of the king), block the check, or capture the attacking piece.
     * Only moves that contribute to resolving the check are retained.
     * </p>
     *
     * @param figureFields The list of possible fields the figure can move to.
     * @param captures The list of fields where an attacking piece can be captured.
     * @param blocks The list of fields that could block the check.
     * @param escapes The list of safe fields the king can escape to.
     * @param figure The figure for which to filter legal moves.
     * @param possibleCaptureSources A list of own figures that are allowed to capture.
     * @param possibleBlockSources A list of own figures that are allowed to block.
     *
     * @return A filtered list of legal moves that resolve the check, or {@code null} if the figure cannot assist.
     */
    public static List<Field> resolveLegalMoves(List<Field> figureFields, List<Field> captures, List<Field> blocks, List<Field> escapes, ChessFigure figure, List<Field> possibleCaptureSources, List<Field> possibleBlockSources) {
        List<Field> sourceList;
        boolean isKing = figure.getClassName().equals(KING_CLASS_NAME);

        sourceList = (isKing) ? handleKingMoves(figureFields, escapes, captures) : handleMoves(figureFields, captures, blocks, figure, possibleCaptureSources, possibleBlockSources);

        return sourceList;
    }

    /**
     * Handles the moves for non-king figures.
     * <p>
     * This method determines whether a given figure can capture or block an attacking piece.
     * Only moves that contribute to resolving the check are retained.
     * </p>
     *
     * @param figureFields The list of possible fields the figure can move to.
     * @param captures The list of fields where an attacking piece can be captured.
     * @param blocks The list of fields that could block the check.
     * @param figure The figure for which to filter legal moves.
     * @param possibleCaptureSources A list of own figures that are allowed to capture.
     * @param possibleBlockSources A list of own figures that are allowed to block.
     *
     * @return A filtered list of legal moves that resolve the check, or {@code null} if the figure cannot assist.
     */
    private static List<Field> handleMoves(List<Field> figureFields, List<Field> captures, List<Field> blocks, ChessFigure figure, List<Field> possibleCaptureSources, List<Field> possibleBlockSources) {
        boolean canCapture = !captures.isEmpty();
        boolean canBlock = !blocks.isEmpty();

        List<String> possibleCaptureSourcesString = turnFieldsToFigureNames(possibleCaptureSources);
        List<String> possibleBlockSourcesString = turnFieldsToFigureNames(possibleBlockSources);

        if (!canCapture && !canBlock) {
            return null;
        }
        if (canCapture) {
            if (canBlock) {
                if (possibleBlockSourcesString.contains(figure.getClassName()) || possibleCaptureSourcesString.contains(figure.getClassName())) {
                    return retainTwoLists(figureFields, blocks, captures);
                } else {
                    return null;
                }
            }
            if (possibleCaptureSourcesString.contains(figure.getClassName())) {
                figureFields.retainAll(captures);
            } else {
                return null;
            }
        } else if (canBlock) {
            if (possibleBlockSourcesString.contains(figure.getClassName())) {
                figureFields.retainAll(blocks);
            } else {
                return null;
            }
        }
        return figureFields;
    }

    /**
     * Handles the moves for the king figure.
     * <p>
     * This method determines whether the king can escape or capture an attacking piece.
     * Only moves that contribute to resolving the check are retained.
     * </p>
     *
     * @param figureFields The list of possible fields the king can move to.
     * @param escapes The list of safe fields the king can escape to.
     * @param captures The list of fields where an attacking piece can be captured.
     *
     * @return A filtered list of legal moves that resolve the check, or {@code null} if the king cannot assist.
     */
    private static List<Field> handleKingMoves(List<Field> figureFields, List<Field> escapes, List<Field> captures) {
        boolean canCapture = !captures.isEmpty();
        boolean canEscape = !escapes.isEmpty();

        if (!canCapture && !canEscape) {
            return null;
        }
        if (canEscape && canCapture) {
            return retainTwoLists(figureFields, captures, escapes);
        } else if (canEscape) {
            figureFields.retainAll(escapes);
        }
        return figureFields;
    }

    /**
     * Converts a list of fields to a list of figure names.
     *
     * @param fields The list of fields to convert.
     * @return A list of figure names corresponding to the figures in the fields.
     */
    private static List<String> turnFieldsToFigureNames(List<Field> fields) {
        List<String> figureNames = new ArrayList<>();
        for (Field field : fields) {
            if (field.figure == null) {
                continue;
            }
            figureNames.add(field.figure.getClassName());
        }
        return figureNames;
    }

    /**
     * joins elements from the source list that are present in either of the two provided lists.
     *
     * @param sourceList The original list to filter.
     * @param list1 The first list to compare against.
     * @param list2 The second list to compare against.
     * @return The modified source list containing elements present in either list1 or list2.
     */
    private static List<Field> retainTwoLists(List<Field> sourceList, List<Field> list1, List<Field> list2) {
        List<Field> double1 = new ArrayList<>();
        List<Field> double2 = new ArrayList<>();

        double1.addAll(sourceList);
        double1.retainAll(list1);

        double2.addAll(sourceList);
        double2.retainAll(list2);

        sourceList.clear();
        sourceList.addAll(double1);
        sourceList.addAll(double2);

        return sourceList;
    }
}
