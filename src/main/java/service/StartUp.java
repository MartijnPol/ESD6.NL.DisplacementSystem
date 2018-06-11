package service;

import domain.CarTracker;
import domain.CarTrackerRule;
import domain.Credentials;
import jms.MessageProducer;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.IOException;
import java.util.*;

@Singleton
@Startup
public class StartUp {

    @Inject
    private CarTrackerService carTrackerService;

    @Inject
    private AuthenticationService authenticationService;

    @Inject
    private LogService logService;

    @Inject
    private JsonReader jsonReader;

    @Inject
    private MessageProducer messageProducer;

    public StartUp() {
    }

    @PostConstruct
    public void initData() {
        CarTracker carTracker = new CarTracker();
        carTracker.setId("NLD1");
        carTracker.setStartDate(new Date());
        carTracker.setManufacturer("ASUS");
        carTracker.setEnabled(true);

        List<CarTrackerRule> carTrackerRules = new ArrayList<CarTrackerRule>();
        carTrackerRules.add(new CarTrackerRule(carTracker, 2L, new GregorianCalendar(2017, Calendar.DECEMBER, 1).getTime(), 51.560596, 5.091914));
        carTrackerRules.add(new CarTrackerRule(carTracker, 3L, new GregorianCalendar(2017, Calendar.DECEMBER, 2).getTime(), 51.523677, 5.064195));
        carTrackerRules.add(new CarTrackerRule(carTracker, 4L, new GregorianCalendar(2017, Calendar.DECEMBER, 3).getTime(), 51.523677, 5.064195));
        carTracker.setRules(carTrackerRules);

        for (CarTrackerRule rule: carTrackerRules)
        {
            rule.setCarTracker(carTracker);
        }
        carTracker.setTotalRules((long) carTrackerRules.size());
        carTrackerService.create(carTracker);

        Credentials credentials = new Credentials("admin");
        credentials.setToken("fmrh7fpci1m6t53618b94iqr0d");
        authenticationService.createCredentials(credentials);
    }
}