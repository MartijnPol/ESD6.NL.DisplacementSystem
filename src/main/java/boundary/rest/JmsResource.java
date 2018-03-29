package boundary.rest;

import domain.CarTracker;
import jms.MessageProducer;
import service.CarTrackerService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;


@Path("jms")
public class JmsResource {
    
    @Inject
    public MessageProducer messageProducer;

    @Inject
    private CarTrackerService carTrackerService;

    @Path("{id}")
    @GET
    public String startJMS(@PathParam("id") Long id) {
        CarTracker carTracker = carTrackerService.findById(id);
        messageProducer.sentMessage(carTracker);

        if (carTracker == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return carTracker.toString();
    }
}
