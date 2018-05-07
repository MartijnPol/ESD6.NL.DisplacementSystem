package boundary.rest;

import org.junit.BeforeClass;
import org.junit.Test;

import static boundary.rest.RestResourceSetup.Setup;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CarTrackerResourceTest {

    private final String token = "q21jk1e0tf839ilbvl4q28lvp9";

    @BeforeClass
    public static void setUp() {
        Setup();
    }

    @Test
    public void carTrackers() {
        given().
                auth().oauth2(token).
                when().
                get("CarTrackers").
                then().
                statusCode(200).
                body("size()", equalTo(1),
                        "[0].CarTrackerId", equalTo(1));
    }

    @Test
    public void carTrackerByID() {
        given().
                when().
                get("CarTrackers/1").
                then().
                statusCode(200).
                body("CarTrackerId", equalTo(1),
                        "CarTrackerRules.size()", equalTo(3));
    }

    @Test
    public void carTrackerRulesID() {
        given().
                when().
                get("CarTrackers/1").
                then().
                statusCode(200).
                body("CarTrackerId", equalTo(1),
                        "CarTrackerRules.size()", equalTo(3),
                        "CarTrackerRules[0].id", equalTo(2),
                        "CarTrackerRules[0].hasDriven", equalTo(true));
    }
}
