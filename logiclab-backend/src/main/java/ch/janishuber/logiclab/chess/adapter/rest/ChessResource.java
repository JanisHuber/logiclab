package ch.janishuber.logiclab.chess.adapter.rest;

import ch.janishuber.logiclab.chess.adapter.persistence.ChessRepository;
import ch.janishuber.logiclab.chess.adapter.rest.dto.FieldDto;
import ch.janishuber.logiclab.chess.adapter.rest.dto.GameDto;
import ch.janishuber.logiclab.chess.adapter.rest.dto.MoveDto;
import ch.janishuber.logiclab.chess.domain.Game;
import ch.janishuber.logiclab.chess.domain.board.BoardMapperHelper;
import ch.janishuber.logiclab.chess.domain.board.Field;
import ch.janishuber.logiclab.chess.domain.enums.FigureColor;
import ch.janishuber.logiclab.chess.domain.evaluate.GameStateHelper;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Path("/chess")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChessResource {

    @Inject
    private ChessRepository chessRepository;

    @POST
    @Path("/new")
    public Response newGame(@QueryParam("againstAI") boolean againstAi, @QueryParam("botColor") FigureColor botColor,  @QueryParam("botDifficulty") int botDifficulty) {
        Game game = Game.startNewGame(againstAi, botColor, botDifficulty);
        if (game.chessController.currentTurn == botColor) {
            Optional<Move> botMove = game.makeBotMove();
            if (botMove.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            StringBuilder strB = new StringBuilder();
            String source = botMove.get().getSource(game.chessController.chessBoard).row + botMove.get().getSource(game.chessController.chessBoard).column;
            String target = botMove.get().getTarget(game.chessController.chessBoard).row + botMove.get().getTarget(game.chessController.chessBoard).column;
            strB.append(source).append("-").append(target);
            game.addMoveToMoveHistory(strB.toString());
            game.setCurrentTurn((game.getCurrentTurn().equals(FigureColor.WHITE.toString())) ? FigureColor.BLACK : FigureColor.WHITE);
        }
        int gameId = chessRepository.save(game);
        GameDto gameDto = new GameDto(gameId, game.getGameState(), game.getBoardState(), game.getCurrentTurn(), game.isAgainstAI(), game.getBotColor().toString());
        return Response.ok(gameDto).build();
    }

    @GET
    @Path("/{id}")
    public Response gameState(@PathParam("id") int gameId) {
        Optional<Game> gameEntity = chessRepository.getGame(gameId);
        if (gameEntity.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Game loadedGame = gameEntity.get();
        GameDto gameDto = new GameDto(gameId, loadedGame.getGameState(), loadedGame.getBoardState(), loadedGame.getCurrentTurn(), loadedGame.isAgainstAI(), loadedGame.getBotColor().toString());
        return Response.ok(gameDto).build();
    }

    @GET
    @Path("/{id}/gameState")
    public Response getGameState(@PathParam("id") int gameId) {
        Optional<Game> game = chessRepository.getGame(gameId);
        if (game.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Game loadedGame = game.get();

        GameStateHelper.getStalemateStatus(loadedGame.chessController.chessBoard, loadedGame.chessController.currentTurn)
                .ifPresent(stalemate -> {
                    if (stalemate) {
                        loadedGame.setGameState("STALEMATE");
                    } else {
                        loadedGame.setGameState("CHECKMATE");
                    }
                });
        chessRepository.updateGame(loadedGame);
        GameDto gameDto = new GameDto(gameId, loadedGame.getGameState(), loadedGame.getBoardState(), loadedGame.getCurrentTurn(), loadedGame.isAgainstAI(), loadedGame.getBotColor().toString());
        return Response.ok(gameDto).build();
    }

    @POST
    @Path("{id}/player/move")
    public Response move(@PathParam("id") int gameId, MoveDto move) {
        Optional<Game> game = chessRepository.getGame(gameId);
        if (game.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Game loadedGame = game.get();

        Optional<Boolean> hasMoved = loadedGame.makeMove(move);
        if (hasMoved.isEmpty()) {
            return Response.status(Response.Status.GONE).build(); // Player lost
        }
        if (!hasMoved.get()) {
            return Response.status(Response.Status.BAD_REQUEST).build(); // Player couldn't move
        }

        StringBuilder strB = new StringBuilder();
        strB.append(move.source()).append("-").append(move.target());
        loadedGame.addMoveToMoveHistory(strB.toString());

        loadedGame.setCurrentTurn((loadedGame.getCurrentTurn().equals(FigureColor.WHITE.toString())) ? FigureColor.BLACK : FigureColor.WHITE);
        chessRepository.updateGame(loadedGame);

        return Response.ok(loadedGame).build();
    }

    @GET
    @Path("/{id}/bot/move")
    public Response moveBot(@PathParam("id") int gameId) {
        Optional<Game> game = chessRepository.getGame(gameId);
        if (game.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Game loadedGame = game.get();

        Optional<Move> botMove = loadedGame.makeBotMove();
        if (botMove.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        StringBuilder strB = new StringBuilder();
        String source = botMove.get().getSource(loadedGame.chessController.chessBoard).row + botMove.get().getSource(loadedGame.chessController.chessBoard).column;
        String target = botMove.get().getTarget(loadedGame.chessController.chessBoard).row + botMove.get().getTarget(loadedGame.chessController.chessBoard).column;
        strB.append(source).append("-").append(target);
        loadedGame.addMoveToMoveHistory(strB.toString());

        loadedGame.setCurrentTurn((loadedGame.getCurrentTurn().equals(FigureColor.WHITE.toString())) ? FigureColor.BLACK : FigureColor.WHITE);
        chessRepository.updateGame(loadedGame);
        return Response.ok(loadedGame).build();
    }

    @GET
    @Path("/{id}/player/fields")
    public Response fields(@PathParam("id") int gameId, @QueryParam("position") String position) {
        Optional<Game> game = chessRepository.getGame(gameId);
        if (game.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Game loadedGame = game.get();

        List<Field> possibleMoves = loadedGame.getPossibleMoves(position);
        if (possibleMoves == null || possibleMoves.isEmpty()) {
            List<FieldDto> response = new ArrayList<>();
            return Response.ok(response).build();
        }
        List<FieldDto> possibleMovesDto = BoardMapperHelper.convertToDTO(possibleMoves);
        return Response.ok(possibleMovesDto).build();
    }
}