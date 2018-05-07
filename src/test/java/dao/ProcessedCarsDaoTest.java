package dao;

import dao.jpa.ProcessedCarsDaoJPAImpl;
import domain.CarTracker;
import domain.CarTrackerRule;
import domain.ProcessedCars;
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

public class ProcessedCarsDaoTest {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("CarTackerTestPU");
    private EntityManager em;
    private EntityTransaction tx;

    private ProcessedCarsDaoJPAImpl processedCarsDao;
    private CarTracker carTracker;
    private List<CarTrackerRule> carTrackerRules;

    private Date dateOne;

    @Before
    public void setUp() {
        try {
            new DatabaseCleaner(emf.createEntityManager()).clean();
        } catch (SQLException ex) {
            Logger.getLogger(ProcessedCarsDaoJPAImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        em = emf.createEntityManager();
        tx = em.getTransaction();

        this.processedCarsDao = new ProcessedCarsDaoJPAImpl();
        processedCarsDao.setEntityManager(em);
        carTracker = new CarTracker();
        carTrackerRules = new ArrayList<>();

        dateOne = new GregorianCalendar(2017, Calendar.DECEMBER, 1).getTime();
    }

    @Test
    public void getNotProcessedDataById_Id_ListOfNotProcessedCars() {
        carTrackerRules.add(new CarTrackerRule(carTracker, 2L, dateOne, 51.560596, 5.091914, true));
        carTracker.setRules(carTrackerRules);

        ProcessedCars processedCars = new ProcessedCars(carTracker, dateOne, false);

        tx.begin();
        processedCarsDao.create(processedCars);
        tx.commit();

        List<ProcessedCars> foundCar = processedCarsDao.getNotProcessedDataById(1L);

        Assert.assertEquals(1, foundCar.size());
        Assert.assertNotEquals(0, foundCar.size());
        Assert.assertNotEquals(2, foundCar.size());
    }
}
