package himyf.controller;

import himyf.entity.Character;
import io.smallrye.mutiny.Uni;
import himyf.controller.request.CharacterRequest;
import himyf.service.HowIMetYourFunctionService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import org.apache.http.HttpStatus;

import java.util.List;

@AllArgsConstructor
@Path("/persist")
public class HowIMetYourFunctionController {

    private final HowIMetYourFunctionService howIMetYourFunctionService;

    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> persist(CharacterRequest characterRequest) {
        return howIMetYourFunctionService.persist(characterRequest)
                .map(ignore -> Response
                        .status(HttpStatus.SC_NO_CONTENT)
                        .build());
    }


    @GET
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Character>> getList() {
        return howIMetYourFunctionService.getAll();
    }
}
