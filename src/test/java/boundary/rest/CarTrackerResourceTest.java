package boundary.rest;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static boundary.rest.RestResourceSetup.Setup;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CarTrackerResourceTest {

//    private final String token = "q21jk1e0tf839ilbvl4q28lvp9";

    @BeforeClass
    public static void setUp() {
        Setup();
    }

    @Ignore
    @Test
    public void carTrackers() {
        given().
//                auth().oauth2(token).
                when().
                get("CarTrackers").
                then().
                statusCode(200).
                body("size()", equalTo(37),
                        "[0].CarTrackerId", equalTo(1));
    }

    @Ignore
    @Test
    public void carTrackerByID() {
        given().
                when().
                get("CarTrackers/1").
                then().
                statusCode(200).
                body("CarTrackerId", equalTo(1),
                        "CarTrackerRules.size()", equalTo(8));
    }
    @Ignore
    @Test
    public void carTrackerRulesID() {
        given().
                when().
                get("CarTrackers/1").
                then().
                statusCode(200).
                body("CarTrackerId", equalTo(1),
                        "CarTrackerRules.size()", equalTo(8),
                        "CarTrackerRules[0].id", equalTo(1),
                        "CarTrackerRules[0].driven", equalTo(true));
    }
}
