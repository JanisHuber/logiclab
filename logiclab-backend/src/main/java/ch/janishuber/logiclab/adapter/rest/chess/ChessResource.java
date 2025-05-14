package ch.janishuber.logiclab.adapter.rest.chess;

import ch.janishuber.logiclab.adapter.persistence.chess.ChessRepository;
import ch.janishuber.logiclab.adapter.rest.chess.dto.FieldDto;
import ch.janishuber.logiclab.adapter.rest.chess.dto.GameDto;
import ch.janishuber.logiclab.adapter.rest.chess.dto.MoveDto;
import ch.janishuber.logiclab.domain.chess.Game;
import ch.janishuber.logiclab.domain.chess.board.BoardMapperHelper;
import ch.janishuber.logiclab.domain.chess.board.Field;
import ch.janishuber.logiclab.domain.chess.enums.FigureColor;
import ch.janishuber.logiclab.domain.chess.util.Move;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
        if (game.isBotTurn()) {
            Optional<Move> botMove = game.makeBotMove();
            if (botMove.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            game.addMoveToMoveHistory(botMove.get());
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

    @POST
    @Path("{id}/player/move")
    public Response move(@PathParam("id") int gameId, MoveDto move) {
        System.out.println("Moving: " + move.promotingFigureClassName());
        Optional<Game> game = chessRepository.getGame(gameId);
        if (game.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Game loadedGame = game.get();

        boolean hasMoved = loadedGame.makeMove(move);
        if (!hasMoved) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        loadedGame.addMoveToMoveHistory(move);
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
        loadedGame.addMoveToMoveHistory(botMove.get());
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

        List<FieldDto> possibleMovesDto = BoardMapperHelper.convertToDTO(possibleMoves);
        return Response.ok(possibleMovesDto).build();
    }

    @GET
    @Path("/{id}/history")
    public Response getMoveHistory(@PathParam("id") int gameId) {
        Optional<Game> game = chessRepository.getGame(gameId);
        if (game.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Game loadedGame = game.get();
        String moveHistory = loadedGame.getMoveHistory();
        return Response.ok(moveHistory).build();
    }
}