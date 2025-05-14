package ch.janishuber.logiclab.domain.chess.evaluate;

import ch.janishuber.logiclab.domain.chess.board.ChessBoard;
import ch.janishuber.logiclab.domain.chess.board.Field;
import ch.janishuber.logiclab.domain.chess.controller.LegalMovesHandler;
import ch.janishuber.logiclab.domain.chess.enums.FigureColor;
import ch.janishuber.logiclab.domain.chess.evaluate.piecetables.PieceTables;
import ch.janishuber.logiclab.domain.chess.figures.Pawn;

import java.util.List;
import java.util.Optional;


public class evaluateBoard {

    public static int evaluateBoard(ChessBoard chessBoard, FigureColor currentTurn, FigureColor botColor) {
        Optional<Boolean> isStalemate = GameStateHelper.getStalemateStatus(chessBoard, currentTurn);
        if (isStalemate.isPresent()) {
            if (isStalemate.get()) {
                return 0;
            } else {
                return checkmateValue(chessBoard, botColor);
            }
        }
        int overallValue = 0;
        GamePhase gamePhase = PhaseEvaluator.evaluatePhase(chessBoard);
        int pieceValue = getPieceValue(chessBoard, botColor);
        int positionValue = getPawnPositionValue(chessBoard, botColor);
        int mobilityValue = getMobilityValue(chessBoard, botColor);

        if (gamePhase == GamePhase.OPENING) {
            overallValue = pieceValue + mobilityValue / 2 + positionValue / 2;
        } else if (gamePhase == GamePhase.MIDDLEGAME) {
            overallValue = pieceValue + mobilityValue / 2 + positionValue / 2;
        } else if (gamePhase == GamePhase.ENDGAME) {
            overallValue = pieceValue + positionValue;
        }
        return overallValue;
    }

    private static int getMobilityValue(ChessBoard chessBoard, FigureColor botColor) {
        int value = 0;
        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() != null) {
                List<Field> possibleMoves = field.getFigure().getPossibleMoves(chessBoard);
                value += (botColor == field.getFigure().figureColor) ? possibleMoves.size() : -possibleMoves.size();
            }
        }
        return value / 2;
    }

    private static int getPieceValue(ChessBoard chessBoard, FigureColor botColor) {
        int value = 0;
        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() == null) continue;
            int figureValue = switch (field.getFigure().getClassName()) {
                case "Pawn" -> 100;
                case "Rook" -> 500;
                case "Knight" -> 300;
                case "Bishop" -> 325;
                case "Queen" -> 900;
                default -> 0;
            };
            if (field.getFigure().figureColor == botColor) {
                value += figureValue;
            } else {
                value -= figureValue;
            }
        }
        return value;
    }

    private static int getPawnPositionValue(ChessBoard chessBoard, FigureColor botColor) {
        int value = 0;

        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() instanceof Pawn) {
                int tempValue = (botColor == field.getFigure().figureColor) ? PieceTables.getPawnTableValue(field) : -PieceTables.getPawnTableValue(field);
                if (isPawnProtected(field, chessBoard)) {
                    tempValue += 10;
                }
                value += tempValue;
            }
        }
        return value / 2;
    }

    private static boolean isPawnProtected(Field field, ChessBoard chessBoard) {
        for (Field currentField : chessBoard.getFields()) {
            if (currentField == field) {
                continue;
            }
            if (currentField.getFigure() != null) {
                if (currentField.getFigure().figureColor == field.getFigure().figureColor) {
                    List<Field> possibleMoves = currentField.getFigure().getPossibleMoves(chessBoard);
                    if (possibleMoves.contains(field)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static int checkmateValue(ChessBoard chessBoard, FigureColor botColor) {
        int counterWhite = 0;
        int counterBlack = 0;

        for (Field field : chessBoard.getFields()) {
            if (field.getFigure() == null) continue;
            LegalMovesHandler lmh = new LegalMovesHandler(chessBoard, field.getFigure().figureColor);
            if (!lmh.getLegalMoves(field.getFigure()).isEmpty()) {
                if (field.getFigure().figureColor == botColor) {
                    counterWhite++;
                } else {
                    counterBlack++;
                }
            }
        }
        if (counterWhite == 0 && counterBlack == 0) {
            return 0;
        } else if (counterWhite == 0) {
            return -1000000;
        } else if (counterBlack == 0) {
            return 1000000;
        }
        return 0;
    }
}
