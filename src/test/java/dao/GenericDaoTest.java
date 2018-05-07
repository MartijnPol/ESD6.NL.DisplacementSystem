package dao;

import dao.jpa.CarTrackerDaoJPAImpl;
import domain.CarTracker;
import domain.CarTrackerRule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.DatabaseCleaner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenericDaoTest {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("CarTackerTestPU");
    private EntityManager em;
    private EntityTransaction tx;

    private CarTrackerDaoJPAImpl carTrackerDao;
    private CarTracker carTracker;
    private List<CarTrackerRule> carTrackerRules;

    private Date dateOne;
    private Date dateTwo;

    @Before
    public void setUp() {
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

        dateOne = new GregorianCalendar(2017, Calendar.DECEMBER, 1).getTime();
        dateTwo = new GregorianCalendar(2017, Calendar.DECEMBER, 2).getTime();
    }

    @Test
    public void create_ClassType_TypeCreated() {
        carTrackerRules.add(new CarTrackerRule(carTracker, 2L, dateOne, 51.560596, 5.091914, true));
        carTracker.setRules(carTrackerRules);

        tx.begin();
        carTrackerDao.create(carTracker);
        tx.commit();

        tx.begin();
        List<CarTracker> foundCarTrackers = carTrackerDao.findAll();
        tx.commit();

        Assert.assertEquals(1, foundCarTrackers.size());
        Assert.assertNotEquals(2, foundCarTrackers.size());
        Assert.assertEquals(1, foundCarTrackers.get(0).getRules().size());
        Assert.assertNotEquals(2, foundCarTrackers.get(0).getRules().size());
        Assert.assertNotEquals(0, foundCarTrackers.get(0).getRules().size());
    }

    @Test
    public void update_ClassType_TypeUpdated() {
        carTrackerRules.add(new CarTrackerRule(carTracker, 2L, dateOne, 51.560596, 5.091914, true));
        carTracker.setRules(carTrackerRules);

        tx.begin();
        carTrackerDao.create(carTracker);
        List<CarTracker> foundCarTrackers = carTrackerDao.findAll();
        tx.commit();

        Assert.assertEquals(1, foundCarTrackers.size());
        Assert.assertNotEquals(2, foundCarTrackers.size());
        Assert.assertEquals(1, foundCarTrackers.get(0).getRules().size());
        Assert.assertNotEquals(2, foundCarTrackers.get(0).getRules().size());
        Assert.assertNotEquals(0, foundCarTrackers.get(0).getRules().size());

        carTrackerRules.add(new CarTrackerRule(carTracker, 2L, dateTwo, 51.560596, 5.091914, true));

        carTracker.setRules(carTrackerRules);

        tx.begin();
        carTrackerDao.update(carTracker);
        tx.commit();

        Assert.assertEquals(1, foundCarTrackers.size());
        Assert.assertNotEquals(2, foundCarTrackers.size());
        Assert.assertEquals(2, foundCarTrackers.get(0).getRules().size());
        Assert.assertNotEquals(1, foundCarTrackers.get(0).getRules().size());
        Assert.assertNotEquals(3, foundCarTrackers.get(0).getRules().size());
    }

    @Test
    public void deleteById_TypeLongId_TypeDeleted() {
        tx.begin();
        carTrackerDao.create(carTracker);
        tx.commit();

        List<CarTracker> foundCarTrackers = carTrackerDao.findAll();

        Assert.assertEquals(1, foundCarTrackers.size());
        Assert.assertNotEquals(0, foundCarTrackers.size());

        tx.begin();
        carTrackerDao.deleteById(carTracker.getId());
        tx.commit();

        List<CarTracker> foundCarTrackersDeleted = carTrackerDao.findAll();

        Assert.assertEquals(0, foundCarTrackersDeleted.size());
        Assert.assertNotEquals(1, foundCarTrackersDeleted.size());
    }

    @Test
    public void delete_Type_TypeDeleted() {
        tx.begin();
        carTrackerDao.create(carTracker);
        tx.commit();

        List<CarTracker> foundCarTrackers = carTrackerDao.findAll();

        Assert.assertEquals(1, foundCarTrackers.size());
        Assert.assertNotEquals(0, foundCarTrackers.size());

        tx.begin();
        carTrackerDao.delete(carTracker);
        tx.commit();

        List<CarTracker> foundCarTrackersDeleted = carTrackerDao.findAll();

        Assert.assertEquals(0, foundCarTrackersDeleted.size());
        Assert.assertNotEquals(1, foundCarTrackersDeleted.size());
    }


    @Test
    public void findById_TypeLongId_TypeFound() {
        tx.begin();
        carTrackerDao.create(carTracker);
        tx.commit();

        CarTracker foundCarTrackers = carTrackerDao.findById(1L);

        Assert.assertEquals(carTracker.getRules(), foundCarTrackers.getRules());
    }

    @Test
    public void findAll_None_ListOfTypeFound() {
        carTrackerRules.add(new CarTrackerRule(carTracker, 2L, dateOne, 51.560596, 5.091914, true));
        carTracker.setRules(carTrackerRules);

        CarTracker carTracker2 = new CarTracker();

        tx.begin();
        carTrackerDao.create(carTracker);
        carTrackerDao.create(carTracker2);
        tx.commit();

        List<CarTracker> foundCarTrackers = carTrackerDao.findAll();

        Assert.assertEquals(2, foundCarTrackers.size());
        Assert.assertNotEquals(0, foundCarTrackers.size());
        Assert.assertNotEquals(1, foundCarTrackers.size());
    }
}
