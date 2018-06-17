package boundary.rest;

import domain.*;
import jms.MessageProducer;
import org.apache.commons.collections4.CollectionUtils;
import service.CarTrackerService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("CarTrackers")
@Stateless
public class CarTrackerResource {

    @Inject
    private CarTrackerService carTrackerService;
    @Inject
    private MessageProducer messageProducer;

    /**
     * Empty constructor
     */
    public CarTrackerResource() {
    }

    /**
     * Function to get all available CarTrackerData that is stored in the database
     * When there is no data found a response status not found is thrown (404).
     *
     * @return all available CarTrackerData stored in the database
     */
    @GET
    @Secured(AuthorizedApplications.AAS)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCarTracker() {
        List<CarTracker> carTrackers = carTrackerService.getCarTrackers();
        if (carTrackers.isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return Response.ok(this.carTrackerService.replaceAllToJson(carTrackers)).build();
    }

    /**
     * Function to create a CarTracker object.
     * When the parameter carTracker is evaluated null a response status not found is thrown (404).
     *
     * @param carTracker CarTracker Json object
     * @return URI containing the newly created CarTracker id
     */
    @POST
    @Path("/Create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured(AuthorizedApplications.AAS)
    public Response createCarTracker(CarTracker carTracker) {
        if (carTracker == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        carTracker.getRules().forEach(carTrackerRule -> carTrackerRule.setCarTracker(carTracker));
        carTrackerService.create(carTracker);

        URI id = URI.create(carTracker.getId().toString());
        return Response.created(id).build();
    }

    /**
     * Function to update a CarTracker entity.
     * When the parameter carTracker is evaluated null a response status not found is thrown (404).
     *
     * @param carTracker CarTracker Json object
     * @return URI containing the newly created CarTracker id
     */
    @POST
    @Path("/Update")
//    @Secured(AuthorizedApplications.AAS)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCarTracker(CarTracker carTracker) {
        if (carTracker == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        if (carTracker.getId() == null || CollectionUtils.isEmpty(carTracker.getRules())) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        messageProducer.sendMessage(carTracker);

        URI id = URI.create(carTracker.getId());
        return Response.created(id).build();
    }

    /**
     * Function to add rules to a CarTracker entity.
     * When the parameter carTrackerRule or ID is evaluated null a response status not found is thrown (404).
     *
     * @param rule
     */
    @POST
    @Path("/AddRule")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void addCarTrackerRules(ReceivedCarTrackerRule rule) {
        if (rule.getId().isEmpty() && rule.getLat() == 0.0 && rule.getLon() == 0.0 && rule.getDate() == null && rule.getMdriven() == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        CarTracker foundCarTracker = carTrackerService.findById(rule.getId());
        CarTrackerRule newRule = new CarTrackerRule(foundCarTracker, rule.getMdriven(), rule.getDate(), rule.getLat(), rule.getLon());
        foundCarTracker.addRule(newRule);
        this.carTrackerService.update(foundCarTracker);
        messageProducer.sendMessage(foundCarTracker);
    }

    /**
     * Function to get CarTrackerData according to a given TrackerId
     * The database is searched for a CarTracker that matches the provided id.
     * When the search return empty a response status not found is thrown (404).
     *
     * @param id trackerId that represents an existing CarTracker
     * @return All data that belongs to the given TrackerId
     */
    @GET
    @Path("{id}")
    @Secured(AuthorizedApplications.AAS)
    public Response getCarTrackerData(@PathParam("id") String id) {
        CarTracker carTracker = carTrackerService.findById(id);

        if (carTracker == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return Response.ok(carTracker.toJson()).build();
    }

//    /**
//     * Function to get CarTrackerData
//     * @param CarTrackerId the id of the CarTracker
//     * @param startDate the from date
//     * @param endDate the end date
//     * @return A JSON response containing the requested CarTrackerData
//     */
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("/getCarTrackerDataWithinPeriod")
//    public Response getCarTrackerDataWithinPeriod(Long CarTrackerId, Date startDate, Date endDate) {
//        CarTracker carTrackerData = carTrackerService.getRulesWithinPeriod(CarTrackerId, startDate, endDate);
//        return Response.ok(carTrackerData).build();
//    }

    /**
     * Function that converts a posted Json file into a CarTrackerDataQuery object
     * and pulls the requested data from the database.
     * When the search returns no elements a response status not found is thrown (404).
     *
     * @param carTrackerDataQueries the given CarTrackerDataQueries
     * @return A JSON file containing a list of objects
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/RulesForPeriods")
    public Response getCarTrackerRulesForMultiplePeriods(CarTrackerDataQuery[] carTrackerDataQueries) {
        List<CarTracker> carTrackerList = carTrackerService.getRulesWithinMultiplePeriods(carTrackerDataQueries);

        if (carTrackerList.isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return Response.ok(this.carTrackerService.replaceAllToJson(carTrackerList)).build();
    }
}
