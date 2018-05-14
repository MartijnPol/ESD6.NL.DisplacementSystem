package boundary.rest;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static boundary.rest.RestResourceSetup.Setup;
import static io.restassured.RestAssured.given;

/**
 * @author Thom van de Pas on 7-5-2018
 */
public class AuthenticationResourceTest {

    @BeforeClass
    public static void setUp() {
        Setup();
    }

    @Test
    public void authenticateApplicationTestSuccessful() {
        Map<String, String> credential = new HashMap<>();
        credential.put("applicationName", "admin");

        given()
                .contentType("application/json")
                .body(credential)
                .when()
                .post("Authentication")
                .then()
                .statusCode(200);
    }

    @Test
    public void authenticateApplicationForbidden() {
        Map<String, String> credential = new HashMap<>();
        credential.put("applicationName", "ForbiddenSystem");

        given()
                .contentType("application/json")
                .body(credential)
                .when()
                .post("Authentication")
                .then()
                .statusCode(403);
    }

}
