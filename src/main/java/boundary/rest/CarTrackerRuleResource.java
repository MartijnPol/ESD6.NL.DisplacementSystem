package boundary.rest;

import domain.CarTrackerRule;
import service.CarTrackerRuleService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

/**
 * @author Thom van de Pas on 16-6-2018
 */
@Path("carTrackerRules")
@Stateless
public class CarTrackerRuleResource {

    @Inject
    private CarTrackerRuleService carTrackerRuleService;

    public CarTrackerRuleResource() {
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/amountOfRulesByCarTrackerId")
    public Response getAmountOfCarTrackerRulesById(@QueryParam("id") String id, @QueryParam("amount") int amount) {
        List<CarTrackerRule> foundRules = this.carTrackerRuleService.getAmountOfRulesByCarTrackerId(id, amount);

        if (foundRules.isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return Response.ok(this.carTrackerRuleService.replaceAllToJson(foundRules)).build();
    }

    @POST
    @Path("/carTrackerIdRoadTypeAndDate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCarTrackerRulesForRoadTypeAndDate(String carTrackerId, String roadType, Date date) {
        if (carTrackerId == null || roadType == null || date == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        List<CarTrackerRule> foundCarTrackerRules = this.carTrackerRuleService.getCarTrackerRulesForDayAndRoadType(carTrackerId, roadType, date);

        if (foundCarTrackerRules.isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return Response.ok(this.carTrackerRuleService.replaceAllToJson(foundCarTrackerRules)).build();
    }

    @POST
    @Path("/carTrackerIdAndDate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCarTrackerRulesForDate(String carTrackerId, Date date) {
        if (carTrackerId == null || date == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        List<CarTrackerRule> foundCarTrackerRules = this.carTrackerRuleService.getCarTrackerRulesForDay(carTrackerId, date);

        if (foundCarTrackerRules.isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return Response.ok(this.carTrackerRuleService.replaceAllToJson(foundCarTrackerRules)).build();
    }
}
