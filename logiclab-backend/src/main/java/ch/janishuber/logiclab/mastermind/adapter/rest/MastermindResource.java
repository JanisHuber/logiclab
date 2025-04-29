package ch.janishuber.logiclab.mastermind.adapter.rest;

import ch.janishuber.logiclab.mastermind.adapter.persistence.GuessRepository;
import ch.janishuber.logiclab.mastermind.adapter.persistence.MastermindRepository;
import ch.janishuber.logiclab.mastermind.adapter.rest.dto.GameDto;
import ch.janishuber.logiclab.mastermind.adapter.rest.dto.GuessDto;
import ch.janishuber.logiclab.mastermind.domain.Game;
import ch.janishuber.logiclab.mastermind.domain.Guess;
import jakarta.inject.Inject;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


import java.util.List;
import java.util.Optional;

@Path("/mastermind")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MastermindResource {

    @Inject
    private MastermindRepository repository;
    @Inject
    private MastermindRepository mastermindRepository;
    @Inject
    private GuessRepository guessRepository;

    @POST
    @Path("/new")
    public Response newGame() {
        Game startedGame = Game.startNewGame();

        int newGameId = repository.save(startedGame);

        GameDto gameDto = new GameDto(newGameId, startedGame.getGameStatus(), new Guess[0]);

        return Response.ok(gameDto).build();
    }

    @GET
    @Path("/{id}")
    public Response gameState(@PathParam("id") int gameId) {
        List<Guess> guesses = guessRepository.getGuesses(gameId);
        Optional<Game> optionalGame = repository.getGame(gameId, guesses.toArray(new Guess[guesses.size()]));

        if (optionalGame.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Game game = optionalGame.get();
        GameDto gameDto = new GameDto(game.getId(), game.getGameStatus(), game.getAttemptsAmount());

        return Response.ok(gameDto).build();
    }

    @POST
    @Path("/{id}/guesses")
    public Response inputGuess(@PathParam("id") int gameId, GuessDto guess) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        var violations = validator.validate(guess);

        if (!violations.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        List<Guess> guesses = guessRepository.getGuesses(gameId);
        Optional<Game> optionalGame = repository.getGame(gameId, guesses.toArray(new Guess[guesses.size()]));

        if (optionalGame.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Game game = optionalGame.get();
        Optional<Guess> guessResult = game.isValidGuess(guess.guess());
        mastermindRepository.updateGame(game);

        if (guessResult.isEmpty()) {
            return Response.status(Response.Status.CONFLICT).build(); // 409
        }
        guessRepository.save(gameId, guessResult.get());

        return Response.ok(guessResult).build();
    }
}