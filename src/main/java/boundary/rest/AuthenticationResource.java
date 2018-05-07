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

@Path("Authentication")
@Stateless
public class AuthenticationResource {

    @Inject
    AuthenticationService authenticationService;

    /**
     * Function for authenticating credentials.
     * The database is searched for existing credentials that match the provided application name.
     * When the search returns empty HTTP error 403 forbidden is thrown.
     * In case that credentials were found a new token will be generated.
     *
     * @param credentials Credentials Json object
     * @return Generated token
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticateApplication(Credentials credentials) {
        String applicationName = credentials.getApplicationName();

        boolean authenticate = authenticationService.authenticate(applicationName);

        if (!authenticate) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        String token = authenticationService.issueToken(applicationName);

        return Response.ok(token).build();
    }
}
