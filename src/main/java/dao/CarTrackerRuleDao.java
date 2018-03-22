package dao;

/**
 * @author Thom van de Pas on 22-3-2018
 */

import domain.CarTracker;
import domain.CarTrackerRule;

/**
 * @author Thom van de Pas on 22-3-2018
 */
public interface CarTrackerRuleDao extends GenericDao<CarTrackerRule> {

    Long getHighestRuleIdFromCarTrackerRules(CarTracker carTracker);
}