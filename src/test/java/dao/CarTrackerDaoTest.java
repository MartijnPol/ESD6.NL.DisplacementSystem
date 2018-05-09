package dao;

import domain.CarTracker;
import domain.CarTrackerRule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class CarTrackerDaoTest {

    @Mock
    private CarTrackerDao carTrackerDao;

    private CarTracker carTracker;
    private List<CarTrackerRule> carTrackerRules;

    private Date dateOne;
    private Date dateTwo;
    private Date dateThree;
    private Date dateFour;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        carTracker = new CarTracker();
        carTracker.setId(1L);
        carTrackerRules = new ArrayList<>();

        dateOne = new GregorianCalendar(2017, Calendar.DECEMBER, 1).getTime();
        dateTwo = new GregorianCalendar(2017, Calendar.DECEMBER, 2).getTime();
        dateThree = new GregorianCalendar(2017, Calendar.DECEMBER, 3).getTime();
        dateFour = new GregorianCalendar(2017, Calendar.DECEMBER, 4).getTime();
    }

    @Test
    public void getRulesWithinPeriod_CarTrackerIDStartDateEndDate_CarTrackerFound() {
        carTrackerDao.create(carTracker);
        verify(carTrackerDao, Mockito.times(1)).create(carTracker);

        when(carTrackerDao.getRulesWithinPeriod(1L, dateOne, dateFour)).thenReturn(carTracker);
        CarTracker foundCarTracker = carTrackerDao.getRulesWithinPeriod(carTracker.getId(), dateOne, dateFour);
        assertThat(foundCarTracker, is(carTracker));

        carTrackerRules.add(new CarTrackerRule(carTracker, 2L, dateOne, 51.560596, 5.091914, true));
        carTracker.setRules(carTrackerRules);
        carTrackerDao.update(carTracker);

        when(carTrackerDao.getRulesWithinPeriod(1L, dateOne, dateFour)).thenReturn(carTracker);
        CarTracker foundCarTracker2 = carTrackerDao.getRulesWithinPeriod(carTracker.getId(), dateOne, dateFour);

        assertEquals(dateOne, foundCarTracker2.getRules().get(0).getDate());
    }

    @Test
    public void getRulesWithinMultiplePeriods_CarTrackerDataQueryList_ListOfCarTrackersFound() {
        carTrackerDao.create(carTracker);
        verify(carTrackerDao, Mockito.times(1)).create(carTracker);

        List<CarTracker> foundCarTrackers = carTrackerDao.findAll();
        verify(carTrackerDao, Mockito.times(1)).findAll();

        carTrackerRules.add(new CarTrackerRule(carTracker, 2L, dateOne, 51.560596, 5.091914, true));
        carTrackerRules.add(new CarTrackerRule(carTracker, 2L, dateTwo, 51.560596, 5.091914, true));
        carTrackerRules.add(new CarTrackerRule(carTracker, 2L, dateThree, 51.560596, 5.091914, true));
        carTrackerRules.add(new CarTrackerRule(carTracker, 2L, dateFour, 51.560596, 5.091914, true));

        carTracker.setRules(carTrackerRules);

        carTrackerDao.update(carTracker);

        assertEquals(4, carTracker.getRules().size());
        assertNotEquals(1, foundCarTrackers.size());
        assertNotEquals(2, foundCarTrackers.size());
        assertNotEquals(3, carTracker.getRules().size());
        assertEquals(dateOne, carTracker.getRules().get(0).getDate());
    }
}
