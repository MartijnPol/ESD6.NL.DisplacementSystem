package service;

import dao.CarTrackerDao;
import dao.ProcessedCarsDao;
import domain.CarTracker;
import domain.CarTrackerDataQuery;
import domain.CarTrackerRule;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CarTrackerServiceTest {

    private CarTrackerService carTrackerService;

    private CarTracker carTracker;
    private List<CarTrackerRule> carTrackerRules;
    private CarTrackerDataQuery[] carTrackerDataQueries;

    private Date dateOne;
    private Date dateTwo;
    private Date dateThree;
    private Date dateFour;

    @Mock
    private CarTrackerDao carTrackerDao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        carTrackerService = new CarTrackerService();
        carTrackerService.setCarTrackerDao(carTrackerDao);

        carTracker = new CarTracker();
        carTrackerRules = new ArrayList<>();
        //carTrackerDataQueries = new CarTrackerDataQuery[]();

        dateOne = new GregorianCalendar(2017, Calendar.DECEMBER, 1).getTime();
        dateTwo = new GregorianCalendar(2017, Calendar.DECEMBER, 2).getTime();
        dateThree = new GregorianCalendar(2017, Calendar.DECEMBER, 3).getTime();
        dateFour = new GregorianCalendar(2017, Calendar.DECEMBER, 4).getTime();
    }

    @Test
    public void create_NewCarTracker_CarTrackerCreated() {
        carTrackerService.create(carTracker);
        verify(carTrackerDao, Mockito.times(1)).create(carTracker);
    }

    @Test
    public void findById_CarTrackerId_CarTrackerFound() {
        when(carTrackerService.findById(1L)).thenReturn(carTracker);
        CarTracker carTrackerFound = carTrackerService.findById(1L);
        assertThat(carTrackerFound, is(carTracker));
    }

    @Test
    public void getCarTrackers_None_CarTrackerListFound() {
        carTrackerService.getCarTrackers();
        verify(carTrackerDao, Mockito.times(1)).findAll();
    }

    @Test
    public void getRulesWithinPeriod_CarTrackerIdAndStartAndEndDate_CarTrackerRulesFoundWithinPeriod() {
        when(carTrackerService.getRulesWithinPeriod(1L, dateOne, dateTwo)).thenReturn(carTracker);
        CarTracker carTrackerFound = carTrackerService.getRulesWithinPeriod(1L, dateOne, dateTwo);
        assertThat(carTrackerFound, is(carTracker));
    }

    @Test
    public void getRulesWithinMultiplePeriods_CarTrackerDataQuery_CarTrackerListFound() {
        //when(carTrackerService.getRulesWithinMultiplePeriods(carTrackerDataQueries))
    }

    @Ignore
    @Test
    public void runAllChecks_CarTracker_None() {
        carTrackerService.runAllChecks(carTracker);
    }

    @Ignore
    @Test
    public void missingRuleValuesCheck_CarTracker_True() {
        carTrackerRules.add(new CarTrackerRule(carTracker, 2L, new GregorianCalendar(2017, Calendar.DECEMBER, 1).getTime(), 51.560596, 5.091914, true));
        carTracker.setRules(carTrackerRules);
        carTracker.setTotalRules(1L);
        assertTrue(carTrackerService.missingRuleValuesCheck(carTracker));
    }

    @Ignore
    @Test
    public void sizeCheck_CarTracker_True() {

    }

    @Ignore
    @Test
    public void idCheck_CarTracker_True() {

    }

    @Ignore
    @Test
    public void storedDataCheck_CarTrackerData_True() {

    }
}

