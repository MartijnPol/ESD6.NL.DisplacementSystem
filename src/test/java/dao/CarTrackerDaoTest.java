package dao;

import dao.jpa.CarTrackerDaoJPAImpl;
import domain.CarTracker;
import domain.CarTrackerDataQuery;
import domain.CarTrackerRule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import util.DatabaseCleaner;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class CarTrackerDaoTest {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("CarTackerTestPU");
    private EntityManager em;
    private EntityTransaction tx;

    private CarTrackerDaoJPAImpl carTrackerDao;
    private CarTracker carTracker;
    private List<CarTrackerRule> carTrackerRules;
    private CarTrackerDataQuery[] carTrackerDataQueries;

    private Date dateOne;
    private Date dateTwo;
    private Date dateThree;
    private Date dateFour;

    @Before
    public void setUp(){
        try {
            new DatabaseCleaner(emf.createEntityManager()).clean();
        } catch (SQLException ex) {
            Logger.getLogger(CarTrackerDaoJPAImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        em = emf.createEntityManager();
        tx = em.getTransaction();

        this.carTrackerDao = new CarTrackerDaoJPAImpl();
        carTrackerDao.setEntityManager(em);
        carTracker = new CarTracker();
        carTrackerRules = new ArrayList<>();
        //carTrackerDataQueries = new CarTrackerDataQuery[]();

        dateOne = new GregorianCalendar(2017, Calendar.DECEMBER, 1).getTime();
        dateTwo = new GregorianCalendar(2017, Calendar.DECEMBER, 2).getTime();
        dateThree = new GregorianCalendar(2017, Calendar.DECEMBER, 3).getTime();
        dateFour = new GregorianCalendar(2017, Calendar.DECEMBER, 4).getTime();
    }

    @Test
    public void getRulesWithinPeriod_CarTrackerIDStartDateEndDate_CarTrackerFound(){
        tx.begin();
        carTrackerDao.create(carTracker);
        CarTracker foundCarTrackers = carTrackerDao.getRulesWithinPeriod(carTracker.getId(), dateOne, dateFour);
        tx.commit();

        carTrackerRules.add(new CarTrackerRule(carTracker, 2L, dateOne, 51.560596, 5.091914, true));

        carTracker.setRules(carTrackerRules);

        tx.begin();
        carTrackerDao.update(carTracker);
        tx.commit();
        CarTracker foundCarTrackers2 = carTrackerDao.getRulesWithinPeriod(carTracker.getId(), dateOne, dateFour);

        Assert.assertNotEquals(2, foundCarTrackers2);
        Assert.assertNotEquals(2, foundCarTrackers2.getRules().size());
        Assert.assertEquals(dateOne, foundCarTrackers2.getRules().get(0).getDate());

        tx.begin();
        foundCarTrackers2.getRules().remove(0);
        carTrackerDao.update(carTracker);
        tx.commit();
    }

    @Test
    public void getRulesWithinMultiplePeriods_CarTrackerDataQueryList_ListOfCarTrackersFound(){
        tx.begin();
        carTrackerDao.create(carTracker);
        List<CarTracker> foundCarTrackers = carTrackerDao.findAll();
        tx.commit();

        carTrackerRules.add(new CarTrackerRule(carTracker, 2L, dateOne, 51.560596, 5.091914, true));
        carTrackerRules.add(new CarTrackerRule(carTracker, 2L, dateTwo, 51.560596, 5.091914, true));
        carTrackerRules.add(new CarTrackerRule(carTracker, 2L, dateThree, 51.560596, 5.091914, true));
        carTrackerRules.add(new CarTrackerRule(carTracker, 2L, dateFour, 51.560596, 5.091914, true));

        carTracker.setRules(carTrackerRules);

        tx.begin();
        carTrackerDao.update(carTracker);
        tx.commit();

        Assert.assertEquals(4, carTracker.getRules().size());
        Assert.assertEquals(1, foundCarTrackers.size());
        Assert.assertNotEquals(2, foundCarTrackers.size());
        Assert.assertNotEquals(3, carTracker.getRules().size());
        Assert.assertEquals(dateOne, carTracker.getRules().get(0).getDate());
    }

    @Ignore
    @Test
    public void getLastRuleId_None_GetLastRuleId(){

    }
}
