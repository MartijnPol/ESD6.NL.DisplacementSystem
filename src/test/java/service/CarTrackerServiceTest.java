package service;

import dao.CarTrackerDao;
import dao.CarTrackerRuleDao;
import dao.ProcessedCarsDao;
import domain.CarTracker;
import domain.CarTrackerRule;
import domain.ProcessedCar;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
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

    private Date dateOne;
    private Date dateTwo;

    private CarTrackerRule carTrackerRule1;
    private CarTrackerRule carTrackerRule2;
    private CarTrackerRule carTrackerRule3;

    @Mock
    private CarTrackerRuleDao carTrackerRuleDao;

    @Mock
    private CarTrackerDao carTrackerDao;

    @Mock
    private ProcessedCarsDao processedCarsDao;

    @Mock
    private CarTrackerService carTrackerServiceMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        carTrackerService = new CarTrackerService();
        carTrackerService.setCarTrackerDao(carTrackerDao);
        carTrackerService.setCarTrackerRuleDao(carTrackerRuleDao);
        carTrackerService.setProcessedCarsDao(processedCarsDao);

        dateOne = new GregorianCalendar(2017, Calendar.DECEMBER, 1).getTime();
        dateTwo = new GregorianCalendar(2017, Calendar.DECEMBER, 2).getTime();
        Date dateThree = new GregorianCalendar(2017, Calendar.DECEMBER, 3).getTime();
        Date dateFour = new GregorianCalendar(2017, Calendar.DECEMBER, 4).getTime();

        carTracker = new CarTracker();
        carTracker.setTotalRules(3L);
        this.carTracker.setId("NLD1");
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
        when(carTrackerService.findById("NLD1")).thenReturn(carTracker);
        CarTracker carTrackerFound = carTrackerService.findById("NLD1");
        assertThat(carTrackerFound, is(carTracker));
    }

    @Test
    public void getCarTrackers_None_CarTrackerListFound() {
        carTrackerService.getCarTrackers();
        verify(carTrackerDao, Mockito.times(1)).findAll();
    }

    @Test
    public void getRulesWithinPeriod_CarTrackerIdAndStartAndEndDate_CarTrackerRulesFoundWithinPeriod() {
        when(carTrackerService.getRulesWithinPeriod("NLD1", dateOne, dateTwo)).thenReturn(carTracker);
        CarTracker carTrackerFound = carTrackerService.getRulesWithinPeriod("NLD1", dateOne, dateTwo);
        assertThat(carTrackerFound, is(carTracker));
    }

    @Test
    public void runAllChecks_CarTracker_None() {
        this.carTracker.addRules(Arrays.asList(carTrackerRule1, carTrackerRule2, carTrackerRule3));
        this.carTrackerService.create(this.carTracker);
        verify(carTrackerDao, Mockito.times(1)).create(carTracker);
        this.carTrackerServiceMock.executeAllCarTrackerChecks(this.carTracker);
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
        this.carTrackerRule1.setMetersDriven(null);
        boolean kmDriven = this.carTrackerService.missingRuleValuesCheck(this.carTracker);

        assertSame(false, kmDriven);

        this.carTrackerRule1.setMetersDriven(2L);
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

    @Test
    public void sizeCheck_CarTracker_CorrectCheck() {
        this.carTrackerRules.add(carTrackerRule1);
        this.carTracker.setRules(this.carTrackerRules);
        this.carTracker.setTotalRules(1L);

        boolean isSizeCorrect = this.carTrackerService.sizeCheck(this.carTracker);

        assertSame(true, isSizeCorrect);

        this.carTrackerRules.add(carTrackerRule2);
        this.carTracker.setRules(this.carTrackerRules);
        this.carTracker.setTotalRules(2L);

        boolean isSizeCorrectTrue = this.carTrackerService.sizeCheck(this.carTracker);

        assertSame(true, isSizeCorrectTrue);
    }

    @Test
    public void idCheck_CarTracker_CorrectCheck() {
        this.carTrackerRule1.setId(2L);
        this.carTrackerRules.add(carTrackerRule1);
        this.carTracker.addRules(carTrackerRules);
        this.carTrackerService.create(carTracker);

        when(carTrackerRuleDao.getHighestRuleIdFromCarTrackerRules(carTracker)).thenReturn(1L);

        boolean idCheckCorrect = this.carTrackerService.idCheck(carTracker);

        assertSame(true, idCheckCorrect);
    }

    @Test
    public void idCheck_CarTracker_FalseCheck() {
        this.carTrackerRule1.setId(2L);
        this.carTrackerRules.add(carTrackerRule1);
        this.carTracker.addRules(carTrackerRules);
        this.carTrackerService.create(carTracker);

        when(carTrackerRuleDao.getHighestRuleIdFromCarTrackerRules(carTracker)).thenReturn(2L);

        boolean idCheckIncorrect = this.carTrackerService.idCheck(carTracker);

        assertSame(false, idCheckIncorrect);
    }

    @Test
    public void idCheck_CarTracker_EmptyRulesCheck() {
        this.carTrackerService.create(carTracker);

        when(carTrackerRuleDao.getHighestRuleIdFromCarTrackerRules(carTracker)).thenReturn(2L);

        boolean idCheckIncorrect = this.carTrackerService.idCheck(carTracker);

        assertSame(false, idCheckIncorrect);
    }

    @Test
    public void executeAllCarTrackerChecks_CarTracker_CorrectCheck() {
        this.carTrackerRule1.setId(2L);
        this.carTrackerRules.add(carTrackerRule1);
        this.carTracker.addRules(carTrackerRules);
        this.carTracker.setTotalRules(1L);
        this.carTracker.setId("NLD1");
        this.carTrackerService.create(carTracker);

        List<ProcessedCar> processedCars = new ArrayList<>();

        when(carTrackerRuleDao.getHighestRuleIdFromCarTrackerRules(carTracker)).thenReturn(1L);
        when(processedCarsDao.getNotProcessedDataById(Matchers.eq("NLD1"))).thenReturn(processedCars);

        boolean allCarTrackerChecks = this.carTrackerService.executeAllCarTrackerChecks(carTracker);

        assertSame(true, allCarTrackerChecks);
    }

    @Test
    public void executeAllCarTrackerChecks_CarTracker_IncorrectCheck() {
        this.carTrackerRule1.setId(2L);
        this.carTrackerRules.add(carTrackerRule1);
        this.carTracker.addRules(carTrackerRules);
        this.carTracker.setTotalRules(1L);
        this.carTracker.setId("NLD1");
        this.carTrackerService.create(carTracker);

        List<ProcessedCar> processedCars = new ArrayList<>();
        processedCars.add(new ProcessedCar());

        when(carTrackerRuleDao.getHighestRuleIdFromCarTrackerRules(carTracker)).thenReturn(1L);
        when(processedCarsDao.getNotProcessedDataById(Matchers.eq("NLD1"))).thenReturn(processedCars);

        boolean allCarTrackerChecks = this.carTrackerService.executeAllCarTrackerChecks(carTracker);

        assertSame(false, allCarTrackerChecks);
    }
}

