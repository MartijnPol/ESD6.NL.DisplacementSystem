package domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CarTrackerTest {
    private CarTracker carTracker;
    private List<CarTrackerRule> carTrackerRules;

    @Before
    public void setUp() {
        carTracker = new CarTracker();
        carTrackerRules = new ArrayList<>();
    }

    @Test
    public void createCarTracker() {
        carTrackerRules.add(new CarTrackerRule(carTracker, 2L, new Date(), 51.560596, 5.091914, true));
        carTrackerRules.add(new CarTrackerRule(carTracker, 3L, new Date(), 51.523677, 5.064195, true));

        carTracker.setRules(carTrackerRules);
        carTracker.setTotalRules((long) carTrackerRules.size());

        Assert.assertEquals(2, carTracker.getRules().size());
        Assert.assertNotEquals(3, carTracker.getRules().size());
        Assert.assertEquals(new Long(2), carTracker.getTotalRules());
    }

    @Test
    public void getCarTrackerRules() {
        carTrackerRules.add(new CarTrackerRule(carTracker, 2L, new Date(), 51.560596, 5.091914, true));

        carTracker.setRules(carTrackerRules);
        carTracker.setTotalRules((long) carTrackerRules.size());

        Assert.assertEquals(1, carTracker.getRules().size());
        Assert.assertNotEquals(2, carTracker.getRules().size());

        List<CarTrackerRule> rules = carTracker.getRules();
        CarTrackerRule firstCarTrackerRule = rules.get(0);

        Long expected = firstCarTrackerRule.getId();

        Assert.assertEquals(new Long(2), firstCarTrackerRule.getKmDriven());
        Assert.assertEquals(expected, firstCarTrackerRule.getId());
        Assert.assertEquals(true, firstCarTrackerRule.isDriven());
    }
}
