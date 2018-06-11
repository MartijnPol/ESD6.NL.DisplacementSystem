package boundary.rest;

import domain.AuthorizedApplications;
import domain.Credentials;
import service.AuthenticationService;
import service.TokenService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("authenticate")
@Stateless
public class AuthenticationResource {
    @Inject
    private TokenService tokenService;

    @GET
    @Path("{AppKey}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response AuthenticateApp(@PathParam("AppKey") String appKey) {

        for (AuthorizedApplications authorizedApplications : AuthorizedApplications.values()) {
            if (authorizedApplications.App().equals(appKey)) {
                return Response.ok(Json.createObjectBuilder().add("Token", tokenService.EncodeToken(appKey)).build()).header("Access-Control-Allow-Origin", "*").build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).header("Access-Control-Allow-Origin", "*").build();

    }
}
