package dao;

import domain.CarTracker;
import domain.CarTrackerRule;
import domain.ProcessedCar;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ProcessedCarDaoTest {

    @Mock
    private ProcessedCarsDao processedCarsDao;

    private CarTracker carTracker;
    private List<ProcessedCar> processedCars;
    private List<CarTrackerRule> carTrackerRules;

    private Date dateOne;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        carTracker = new CarTracker();
        carTrackerRules = new ArrayList<>();
        processedCars = new ArrayList<>();

        dateOne = new GregorianCalendar(2017, Calendar.DECEMBER, 1).getTime();
    }

    @Test
    public void getNotProcessedDataById_Id_ListOfNotProcessedCars() {
        carTrackerRules.add(new CarTrackerRule(carTracker, 2L, dateOne, 51.560596, 5.091914, true));
        carTracker.setRules(carTrackerRules);

        processedCars.add(new ProcessedCar(carTracker, dateOne, false));

        processedCarsDao.create(processedCars.get(0));

        when(processedCarsDao.getNotProcessedDataById("NLD1")).thenReturn(processedCars);
        List<ProcessedCar> foundCars = processedCarsDao.getNotProcessedDataById("NLD1");

        Assert.assertEquals(1, foundCars.size());
        Assert.assertNotEquals(0, foundCars.size());
        Assert.assertNotEquals(2, foundCars.size());
    }
}
