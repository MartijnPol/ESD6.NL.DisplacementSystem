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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Thom van de Pas on 7-5-2018
 */
@RunWith(MockitoJUnitRunner.class)
public class CarTrackerRuleDaoTest {

    @Mock
    private CarTrackerRuleDao carTrackerRuleDao;
    @Mock
    private CarTrackerDao carTrackerDao;

    private CarTracker carTracker;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Date dateOne = new GregorianCalendar(2017, Calendar.DECEMBER, 1).getTime();

        this.carTracker = new CarTracker();
        this.carTracker.setId(1L);

        CarTrackerRule carTrackerRule = new CarTrackerRule(this.carTracker, 2L, dateOne, 51.560596, 5.091914, true);
        carTrackerRule.setId(1L);
        CarTrackerRule carTrackerRule2 = new CarTrackerRule(this.carTracker, 2L, dateOne, 51.560596, 5.091914, true);
        carTrackerRule2.setId(2L);
    }

    @Test
    public void getHighestCarTrackerRuleIdFromCarTrackerTest() {
        this.carTrackerDao.create(carTracker);
        verify(carTrackerDao, Mockito.times(1)).create(carTracker);

        when(this.carTrackerRuleDao.getHighestRuleIdFromCarTrackerRules(carTracker)).thenReturn(2L);
        Long foundHighestRuleIdFromCarTrackerRules = this.carTrackerRuleDao.getHighestRuleIdFromCarTrackerRules(carTracker);
        assertThat(foundHighestRuleIdFromCarTrackerRules, is(2L));
        assertNotSame(1L, foundHighestRuleIdFromCarTrackerRules);
    }
}
