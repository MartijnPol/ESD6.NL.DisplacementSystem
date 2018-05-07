package boundary.rest;

import dao.CarTrackerRuleDao;
import dao.JPA;
import domain.CarTracker;
import domain.CarTrackerRule;
import jms.MessageProducer;
import service.CarTrackerService;
import service.JsonReader;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


@Path("jms")
public class JmsResource {

    @Inject
    private JsonReader jsonReader;

    @Inject
    public MessageProducer messageProducer;

    @Inject
    private CarTrackerService carTrackerService;

    @Inject
    @JPA
    private CarTrackerRuleDao carTrackerRuleDao;


    @Path("{id}")
    @GET
    public String startJMS(@PathParam("id") Long id) {
        CarTracker carTracker = carTrackerService.findById(id);
        messageProducer.sendMessage(carTracker);

        if (carTracker == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return carTracker.toString();
    }

    @GET
    @Path("/TestCartracker")
    public String TestCarTracker() {

        long id = 1;
        CarTracker carTrackerTest = carTrackerService.findById(id);
        System.out.println(carTrackerTest.toString());
        List<CarTrackerRule> carTrackerRulesTest = new ArrayList<>();
        carTrackerRulesTest.add(new CarTrackerRule(carTrackerTest, 5L, new GregorianCalendar(2018, Calendar.JANUARY, 1).getTime(), 55.560596, 6.091914, true));
        carTrackerRulesTest.add(new CarTrackerRule(carTrackerTest, 6L, new GregorianCalendar(2018, Calendar.FEBRUARY, 2).getTime(), 53.523677, 7.064195, true));
        carTrackerRulesTest.add(new CarTrackerRule(carTrackerTest, 8L, new GregorianCalendar(2018, Calendar.OCTOBER, 3).getTime(), 52.523677, 8.064195, true));

        carTrackerTest.addRules(carTrackerRulesTest);
        carTrackerTest.setTotalRules(carTrackerTest.getTotalRules() + carTrackerRulesTest.size());
        for (CarTrackerRule rule : carTrackerRulesTest) {
            carTrackerRuleDao.create(rule);
        }

        messageProducer.sendMessage(carTrackerTest);

        return "Test cartracker ingevoerd";
    }

    @GET
    @Path("/TestJsonCartracker")
    public String TestJsonCarTracker() {

        CarTracker carTracker = null;
        try {
            carTracker = jsonReader.readJsonFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }


        messageProducer.sendMessage(carTracker);

        return "Test cartracker json ingevoerd";
    }
}