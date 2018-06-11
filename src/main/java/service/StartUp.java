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
//        CarTracker carTracker = new CarTracker();
//        carTracker.setId("NLD1");
//        carTracker.setStartDate(new Date());
//        carTracker.setManufacturer("ASUS");
//        carTracker.setEnabled(true);

//        List<CarTrackerRule> carTrackerRules = new ArrayList<CarTrackerRule>();
//        carTrackerRules.add(new CarTrackerRule(carTracker, 151L, new GregorianCalendar(2017, Calendar.DECEMBER, 1).getTime(), 51.560596, 5.091914));
//        carTracker.setRules(carTrackerRules);
//
//        for (CarTrackerRule rule: carTrackerRules)
//        {
//            rule.setCarTracker(carTracker);
//        }
//        carTracker.setTotalRules((long) carTrackerRules.size());
//        carTrackerService.create(carTracker);
//
//        CarTracker carTracker1 = new CarTracker();
//        carTracker1.setId("NLD2");
//        carTracker1.setStartDate(new Date());
//        carTracker1.setManufacturer("Apple");
//        carTracker1.setEnabled(true);
//
//        List<CarTrackerRule> carTrackerRules1 = new ArrayList<CarTrackerRule>();
//        carTrackerRules1.add(new CarTrackerRule(carTracker1, 151L, new GregorianCalendar(2018, Calendar.JUNE, 11).getTime(), 51.52322, 5.06485));
//        carTracker1.setRules(carTrackerRules1);
//
//        for (CarTrackerRule rule: carTrackerRules1)
//        {
//            rule.setCarTracker(carTracker1);
//        }
//        carTracker1.setTotalRules((long) carTrackerRules1.size());
//        carTrackerService.create(carTracker1);
////
//        try {
//            messageProducer.sendMessage(jsonReader.readJsonFiles());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        Credentials credentials = new Credentials("admin");
//        credentials.setToken("fmrh7fpci1m6t53618b94iqr0d");
//        authenticationService.createCredentials(credentials);
    }
}