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
    private boolean againstAI;
    private FigureColor botColor;
    private int botDifficulty;
    private String moveHistory;
    public final transient ChessController chessController;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final int GAME_ID_NOT_SET = -1;

    private Game(int gameId, String gameState, ChessController chessController, boolean againstAI, FigureColor botColor,
            int botDifficulty, String moveHistory) {
        this.gameId = gameId;
        this.gameState = gameState;
        this.chessController = chessController;
        this.currentTurn = chessController.currentTurn.toString();
        this.againstAI = againstAI;
        this.botColor = botColor;
        this.botDifficulty = botDifficulty;
        this.moveHistory = moveHistory;
    }

    public static Game ofExisting(int gameId, String gameState, String boardState, String currentTurn,
            boolean againstAI, FigureColor botColor, int botDifficulty, String moveHistory) {
        FigureColor figureColor = FigureColor.valueOf(currentTurn);
        ChessBoard chessBoard = ChessBoard.ofExisting(boardState);
        ChessController chessController = ChessController.ofExisting(chessBoard, figureColor, againstAI, botColor,
                botDifficulty, moveHistory);
        return new Game(gameId, gameState, chessController, againstAI, botColor, botDifficulty, moveHistory);
    }

    public static Game startNewGame(boolean againstAI, FigureColor botColor, int botDifficulty) {
        ChessController chessController = new ChessController(againstAI, false, botColor, botDifficulty, "");
        return new Game(GAME_ID_NOT_SET, "NEW", chessController, againstAI, botColor, botDifficulty, "");
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

    public Optional<Move> makeBotMove() {
        return chessController.makeBotMove();
    }

    public int getGameId() {
        return gameId;
    }

    public String getMoveHistory() {
        return this.moveHistory;
    }

    public void addMoveToMoveHistory(String move) {
        if (this.moveHistory.isEmpty()) {
            this.moveHistory = move;
        } else {
            this.moveHistory += "," + move;
        }
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

    public boolean isAgainstAI() {
        return againstAI;
    }

    public FigureColor getBotColor() {
        return botColor;
    }

    public int getBotDifficulty() {
        return botDifficulty;
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
