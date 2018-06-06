package domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CarTrackerRuleTest {
    private CarTracker carTracker;
    private List<CarTrackerRule> carTrackerRules;

    @Before
    public void setUp() {
        carTracker = new CarTracker();
        carTrackerRules = new ArrayList<>();
    }

    @Test
    public void createCarTrackerRule() {
        carTrackerRules.add(new CarTrackerRule(carTracker, 2L, new Date(), 51.560596, 5.091914));

        carTracker.setRules(carTrackerRules);
        carTracker.setTotalRules((long) carTrackerRules.size());
        carTracker.setId("NLD1");

        Assert.assertEquals(1, carTracker.getRules().size());
        Assert.assertNotEquals(2, carTracker.getRules().size());
        Assert.assertEquals(new Long(1), carTracker.getTotalRules());
    }
}
