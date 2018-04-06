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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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

        List<CarTrackerRule> carTrackerRules = new ArrayList<CarTrackerRule>();
        carTrackerRules.add(new CarTrackerRule(carTracker, 2L, new GregorianCalendar(2017, Calendar.DECEMBER, 1).getTime(), 51.560596, 5.091914, true));
        carTrackerRules.add(new CarTrackerRule(carTracker, 3L, new GregorianCalendar(2017, Calendar.DECEMBER, 2).getTime(), 51.523677, 5.064195, true));
        carTrackerRules.add(new CarTrackerRule(carTracker, 4L, new GregorianCalendar(2017, Calendar.DECEMBER, 3).getTime(), 51.523677, 5.064195, true));
        carTracker.setRules(carTrackerRules);

        CarTracker test = null;
        try {
            test = jsonReader.readJsonFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
        carTrackerService.create(carTracker);

        messageProducer.sendMessage(test);

        Credentials credentials = new Credentials("admin");
        credentials.setToken("fmrh7fpci1m6t53618b94iqr0d");
        authenticationService.createCredentials(credentials);
    }
}
