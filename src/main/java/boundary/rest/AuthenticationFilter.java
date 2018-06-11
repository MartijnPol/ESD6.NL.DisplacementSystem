package boundary.rest;

import domain.AuthorizedApplications;
import service.AuthenticationService;
import service.TokenService;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    @Context
    private ResourceInfo resourceInfo;

    @Inject
    private TokenService tokenService;

    @Override
    public void filter(ContainerRequestContext requestContext) {

        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        try {
            String token = authorizationHeader.substring("Bearer".length()).trim();
            if(tokenService.CheckIfTokenIsTrusted(token)){
                String app = tokenService.getAppFromToken(token);
                Class<?> resourceClass = resourceInfo.getResourceClass();
                List<AuthorizedApplications> classRoles = extractRoles(resourceClass);

                Method resourceMethod = resourceInfo.getResourceMethod();
                List<AuthorizedApplications> methodRoles = extractRoles(resourceMethod);
                boolean allowed = methodRoles.isEmpty()? checkPermissions(classRoles,app): checkPermissions(methodRoles,app);
                if(!allowed){
                    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).header("Access-Control-Allow-Origin", "*").build());
                }
            }
            else{
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).header("Access-Control-Allow-Origin", "*").build());
            }
        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).header("Access-Control-Allow-Origin", "*").build());
        }

    }

    private List<AuthorizedApplications> extractRoles(AnnotatedElement annotatedElement) {
        if (annotatedElement == null) {
            return new ArrayList<>();
        } else {
            Secured secured = annotatedElement.getAnnotation(Secured.class);
            if (secured == null) {
                return new ArrayList<>();
            } else {
                AuthorizedApplications[] allowedRoles = secured.value();
                return Arrays.asList(allowedRoles);
            }
        }
    }

    private boolean checkPermissions(List<AuthorizedApplications> allowedRoles, String appName) {
        for (AuthorizedApplications allowedRole : allowedRoles) {
            if(allowedRole.App().equals(appName)){
                return true;
            }
        }
        return false;
    }
}
