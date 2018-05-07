package service;

import dao.CarTrackerDao;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertSame;
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

    private CarTrackerRule carTrackerRule1;
    private CarTrackerRule carTrackerRule2;
    private CarTrackerRule carTrackerRule3;
    @Mock
    private CarTrackerDao carTrackerDao;

    @Mock
    private CarTrackerService carTrackerServiceMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        carTrackerService = new CarTrackerService();
        carTrackerService.setCarTrackerDao(carTrackerDao);

        dateOne = new GregorianCalendar(2017, Calendar.DECEMBER, 1).getTime();
        dateTwo = new GregorianCalendar(2017, Calendar.DECEMBER, 2).getTime();
        dateThree = new GregorianCalendar(2017, Calendar.DECEMBER, 3).getTime();
        dateFour = new GregorianCalendar(2017, Calendar.DECEMBER, 4).getTime();

        carTracker = new CarTracker();
        this.carTracker.setId(1L);
        carTrackerRules = new ArrayList<>();
        this.carTrackerRule1 =
                new CarTrackerRule(carTracker, 2L, dateOne, 51.560596, 5.091914, true);
        this.carTrackerRule2 =
                new CarTrackerRule(carTracker, 3L, dateTwo, 51.560596, 5.091914, true);
        this.carTrackerRule3 =
                new CarTrackerRule(carTracker, 4L, dateThree, 51.560596, 5.091914, true);
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

    @Test
    public void runAllChecks_CarTracker_None() {
        this.carTracker.addRules(Arrays.asList(carTrackerRule1, carTrackerRule2, carTrackerRule3));
        this.carTrackerService.create(this.carTracker);
        verify(carTrackerDao, Mockito.times(1)).create(carTracker);
        this.carTrackerServiceMock.runAllChecks(this.carTracker);
    }

    @Test
    public void missingRuleValuesCheck_CarTracker_True() {
        this.carTrackerRule1.setId(1L);
        this.carTrackerRules.add(this.carTrackerRule1);
        this.carTracker.setRules(this.carTrackerRules);
        boolean missingValues = this.carTrackerService.missingRuleValuesCheck(this.carTracker);

        assertSame(true, missingValues);
    }

    @Test
    public void missingRuleValuesCheck_CarTracker_False() {
        this.carTrackerRule1.setId(1L);
        this.carTrackerRules.add(this.carTrackerRule1);
        this.carTracker.setRules(this.carTrackerRules);
        this.carTrackerRule1.setKmDriven(null);
        boolean kmDriven = this.carTrackerService.missingRuleValuesCheck(this.carTracker);

        assertSame(false, kmDriven);

        this.carTrackerRule1.setKmDriven(2L);
        this.carTrackerRule1.setDate(null);
        boolean date = this.carTrackerService.missingRuleValuesCheck(this.carTracker);

        assertSame(false, date);

        this.carTrackerRule1.setDate(new Date());
        this.carTrackerRule1.setLat(0);
        boolean lat = this.carTrackerService.missingRuleValuesCheck(this.carTracker);
        assertSame(false, lat);

        this.carTrackerRule1.setLat(234);
        this.carTrackerRule1.setLon(0);
        boolean lon = this.carTrackerService.missingRuleValuesCheck(carTracker);
        assertSame(false, lon);
    }

    @Test
    public void missingRuleValuesCheck_CarTracker_FalseDate() {
        this.carTrackerRule1.setId(1L);
        this.carTrackerRules.add(this.carTrackerRule1);
        this.carTracker.setRules(this.carTrackerRules);
        this.carTrackerRule1.setDate(null);
        boolean missingValues = this.carTrackerService.missingRuleValuesCheck(this.carTracker);

        assertSame(false, missingValues);
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

