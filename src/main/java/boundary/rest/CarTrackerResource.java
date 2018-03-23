package boundary.rest;

import domain.CarTracker;
import domain.CarTrackerDataQuery;
import service.CarTrackerService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("carTrackers")
@Stateless
public class CarTrackerResource {

    @Inject
    private CarTrackerService carTrackerService;

    public CarTrackerResource() {
    }

    /**
     * Function to get all available CarTrackerData stored in the database
     *
     * @return all available CarTrackerData stored in the database
     */
    @GET
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
     * When the parameter carTracker is evaluated null a response status not found is thrown
     *
     * @param carTracker CarTracker Json object
     * @return URI containing the newly created CarTracker id
     */
    @POST
    @Path("/createCarTracker")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCarTracker(CarTracker carTracker) {
        if (carTracker == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        carTrackerService.create(carTracker);
        URI id = URI.create(carTracker.getId().toString());
        return Response.created(id).build();
    }

    /**
     * Function to get CarTrackerData according to a given TrackerId
     * The database is searched for a cartracker that matches the provided id.
     * When the search return empty a response status not found is thrown.
     *
     * @param id trackerId that represents an existing CarTracker
     * @return All data that belongs to the given TrackerId
     */
    @GET
    @Path("/carTrackerData")
    public Response getCarTrackerData(@QueryParam("id") Long id) {
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
     * Function that converts a posted JSON file into a CarTrackerDataQuery object
     * and pulls the requested data from the database
     *
     * @param carTrackerDataQueries the given CarTrackerDataQueries
     * @return A JSON file containing a list of CarTrackerDataResponse objects
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/getCarTrackerRulesForMultiplePeriods")
    public Response getCarTrackerRulesForMultiplePeriods(CarTrackerDataQuery[] carTrackerDataQueries) {
        List<CarTracker> carTrackerList = carTrackerService.getRulesWithinMultiplePeriods(carTrackerDataQueries);
        return Response.ok(this.carTrackerService.replaceAllToJson(carTrackerList)).build();
    }
}
