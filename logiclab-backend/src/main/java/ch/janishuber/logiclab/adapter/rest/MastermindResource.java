package ch.janishuber.logiclab.adapter.rest;

import ch.janishuber.logiclab.adapter.rest.dto.GameDto;
import ch.janishuber.logiclab.adapter.rest.dto.GuessDto;
import ch.janishuber.logiclab.domain.GenerateNewGame;
import ch.janishuber.logiclab.domain.GetGameState;
import ch.janishuber.logiclab.domain.Guess;
import ch.janishuber.logiclab.domain.ValidateGuess;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


import java.util.Optional;

@Path("/mastermind")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MastermindResource {

    @POST
    @Path("/new")
    public Response newGame() {
        GenerateNewGame generator = new GenerateNewGame();

        Optional<GameDto> gameDto = generator.getGameDto();

        if (gameDto.isEmpty()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(gameDto).build();
    }

    @GET
    @Path("/{id}")
    public Response gameState(@PathParam("id") int gameId) {
        GetGameState gameState = new GetGameState();

        Optional<GameDto> gameDto = gameState.getGameState(gameId);
        if (gameDto.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(gameDto.get()).build();
    }

    @POST
    @Path("/{id}/guesses")
    public Response inputGuess(@PathParam("id") int gameId, GuessDto guess) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        var violations = validator.validate(guess);

        if (!violations.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Optional<Guess> responseGuess = ValidateGuess.validateGuess(gameId, guess);

        if (responseGuess.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(responseGuess).build();
    }
}