package boundary.rest;

import domain.Credentials;
import service.AuthenticationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("authentication")
@Stateless
public class AuthenticationResource {

    @Inject
    AuthenticationService authenticationService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticateUser(Credentials credentials) {
        String applicationName = credentials.getApplicationName();

        boolean authenticate = authenticationService.authenticate(applicationName);

        if(authenticate != true) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        String token = authenticationService.issueToken(applicationName);

        return Response.ok(token).build();
    }
}
