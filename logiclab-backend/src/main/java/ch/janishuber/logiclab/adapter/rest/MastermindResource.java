package ch.janishuber.logiclab.adapter.rest;

import ch.janishuber.logiclab.adapter.rest.dto.GameDto;
import ch.janishuber.logiclab.domain.GenerateNewGame;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


import java.io.IOException;

@Path("/mastermind")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MastermindResource {

    @GET
    @Path("/new")
    public Response newGame() throws IOException {
        GenerateNewGame generator = new GenerateNewGame();

        GameDto gameDto = generator.getGameDto();
        return Response.ok(gameDto).build();
    }
}

