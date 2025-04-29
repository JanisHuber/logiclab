package ch.janishuber.logiclab.chess.domain;

import ch.janishuber.logiclab.chess.adapter.rest.dto.MoveDto;
import ch.janishuber.logiclab.chess.domain.board.BoardMapperHelper;
import ch.janishuber.logiclab.chess.domain.board.ChessBoard;
import ch.janishuber.logiclab.chess.domain.board.Field;
import ch.janishuber.logiclab.chess.domain.controller.ChessController;
import ch.janishuber.logiclab.chess.domain.enums.FigureColor;
import ch.janishuber.logiclab.chess.domain.util.Move;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

public class Game {
    private final int gameId;
    private String gameState;
    private String currentTurn;
    public final transient ChessController chessController;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final int GAME_ID_NOT_SET = -1;

    private Game(int gameId, String gameState, ChessController chessController) {
        this.gameId = gameId;
        this.gameState = gameState;
        this.chessController = chessController;
        this.currentTurn = chessController.currentTurn.toString();
    }

    public static Game ofExisting(int gameId, String gameState, String boardState, String currentTurn) {
        FigureColor figureColor = FigureColor.valueOf(currentTurn);
        ChessBoard chessBoard = ChessBoard.ofExisting(boardState);
        ChessController chessController = ChessController.ofExisting(chessBoard, figureColor);
        return new Game(gameId, gameState, chessController);
    }

    public static Game startNewGame(boolean againstAI) {
        ChessController chessController = new ChessController(againstAI, false);
        return new Game(GAME_ID_NOT_SET, "NEW", chessController);
    }

    public Optional<Boolean> makeMove(MoveDto moveDto) {
        Move move = convertToMove(moveDto);
        return chessController.move(move.getSource(chessController.chessBoard), move.getTarget(chessController.chessBoard));
    }

    public List<Field> getPossibleMoves(String position) {
        String figureSourceRow = String.valueOf(position.charAt(0));
        int figureSourceColumn = Integer.parseInt(position.substring(1));
        Field field = chessController.chessBoard.getField(figureSourceRow, figureSourceColumn);

        return chessController.getCheckedMove(field.figure);
    }

    public Optional<Boolean> makeBotMove() {
        return chessController.makeBotMove();
    }

    public int getGameId() {
        return gameId;
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public void setCurrentTurn(FigureColor figureColor) {
        this.currentTurn = figureColor.toString();
    }

    public String getBoardState() {
        List<Field> boardStateFields = BoardMapperHelper.convertToJson(chessController.chessBoard.getFields());
        String boardState = "";
        try {
            boardState = objectMapper.writeValueAsString(boardStateFields);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return boardState;
    }

    public String getCurrentTurn() {
        return this.currentTurn;
    }

    private Move convertToMove(MoveDto moveDto) {
        char moveSourceRow = moveDto.source().charAt(0);
        int moveSourceColumn = Integer.parseInt(moveDto.source().substring(1));
        char moveTargetRow = moveDto.target().charAt(0);
        int moveTargetColumn = Integer.parseInt(moveDto.target().substring(1));

        Field sourceField = chessController.chessBoard.getField(Character.toString(moveSourceRow), moveSourceColumn);
        Field targetField = chessController.chessBoard.getField(Character.toString(moveTargetRow), moveTargetColumn);
        return new Move(sourceField, targetField);
    }
}
