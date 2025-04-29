package ch.janishuber.logiclab.chess.adapter.rest;

import ch.janishuber.logiclab.chess.adapter.rest.dto.GameDto;
import ch.janishuber.logiclab.mastermind.adapter.persistence.GuessRepository;
import ch.janishuber.logiclab.mastermind.adapter.persistence.MastermindRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/chess")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChessResource {

    @Inject
    private MastermindRepository repository;
    @Inject
    private MastermindRepository mastermindRepository;
    @Inject
    private GuessRepository guessRepository;

    @POST
    @Path("/new")
    public Response newGame() {
        GameDto gameDto = new GameDto();
        return Response.ok(gameDto).build();
    }
}