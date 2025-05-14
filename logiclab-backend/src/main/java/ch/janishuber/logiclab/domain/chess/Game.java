package ch.janishuber.logiclab.domain.chess;

import ch.janishuber.logiclab.adapter.rest.chess.dto.MoveDto;
import ch.janishuber.logiclab.domain.chess.board.BoardMapperHelper;
import ch.janishuber.logiclab.domain.chess.board.ChessBoard;
import ch.janishuber.logiclab.domain.chess.board.Field;
import ch.janishuber.logiclab.domain.chess.controller.ChessController;
import ch.janishuber.logiclab.domain.chess.enums.FigureColor;
import ch.janishuber.logiclab.domain.chess.evaluate.GameStateHelper;
import ch.janishuber.logiclab.domain.chess.figures.Queen;
import ch.janishuber.logiclab.domain.chess.util.Move;
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

    public static Game ofExisting(int gameId, String gameState, String boardState, String currentTurn, boolean againstAI, FigureColor botColor, int botDifficulty, String moveHistory) {
        FigureColor figureColor = FigureColor.valueOf(currentTurn);
        ChessBoard chessBoard = ChessBoard.ofExisting(boardState);
        ChessController chessController = ChessController.ofExisting(chessBoard, figureColor, againstAI, botColor, moveHistory, botDifficulty);
        return new Game(gameId, gameState, chessController, againstAI, botColor, botDifficulty, moveHistory);
    }

    public static Game startNewGame(boolean againstAI, FigureColor botColor, int botDifficulty) {
        ChessController chessController = ChessController.startNewGame(againstAI, botColor, "", botDifficulty);
        return new Game(GAME_ID_NOT_SET, "NEW", chessController, againstAI, botColor, botDifficulty, "");
    }

    public boolean makeMove(MoveDto moveDto) {
        Move move = convertToMove(moveDto);
        return makeMove(move, moveDto.promotingFigureClassName());
    }

    public boolean makeMove(Move move, String promotingFigureClassName) {
        boolean hasMoved = chessController.moveOnChessboard(move, promotingFigureClassName);
        GameStateHelper.getStalemateStatus(chessController.chessBoard, chessController.currentTurn)
                .ifPresent(stalemate -> {
                    if (stalemate) {
                        setGameState("STALEMATE");
                    } else {
                        setGameState("CHECKMATE");
                    }
                });
        setCurrentTurn((getCurrentTurn().equals(FigureColor.WHITE.toString())) ? FigureColor.BLACK : FigureColor.WHITE);
        return hasMoved;
    }

    public List<Field> getPossibleMoves(String position) {
        String figureSourceRow = String.valueOf(position.charAt(0));
        int figureSourceColumn = Integer.parseInt(position.substring(1));
        Field field = chessController.chessBoard.getField(figureSourceRow, figureSourceColumn);

        return chessController.getLegalMoves(field.getFigure());
    }

    public Optional<Move> makeBotMove() {
        Optional<Move> botMove = chessController.getBotMove();
        if (botMove.isEmpty()) {
            return Optional.empty();
        }
        makeMove(botMove.get(), "Queen");
        return botMove;
    }

    public int getGameId() {
        return gameId;
    }

    public String getMoveHistory() {
        return this.moveHistory;
    }

    public void addMoveToMoveHistory(Move move) {
        StringBuilder strB = new StringBuilder();
        String source = move.getSource(chessController.chessBoard).getColumn() + move.getSource(chessController.chessBoard).getRow();
        String target = move.getTarget(chessController.chessBoard).getColumn() + move.getTarget(chessController.chessBoard).getRow();
        strB.append(source).append("-").append(target);

        if (this.moveHistory.isEmpty()) {
            this.moveHistory = strB.toString();
        } else {
            this.moveHistory += "," + strB;
        }
    }

    public void addMoveToMoveHistory(MoveDto moveDto) {
        addMoveToMoveHistory(convertToMove(moveDto));
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

    public boolean isBotTurn() {
        return this.againstAI && this.currentTurn.equals(this.botColor.toString());
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
