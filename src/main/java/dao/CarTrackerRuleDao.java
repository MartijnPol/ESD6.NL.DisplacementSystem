package dao;


import domain.CarTracker;
import domain.CarTrackerRule;

import java.util.Date;
import java.util.List;

/**
 * @author Thom van de Pas on 22-3-2018
 */
public interface CarTrackerRuleDao extends GenericDao<CarTrackerRule> {

    Long getHighestRuleIdFromCarTrackerRules(CarTracker carTracker);

    List<CarTrackerRule> getRulesByIDMonthAndYear(String id, int month, int year);

    List<CarTrackerRule> getCarTrackerRulesForDayAndRoadType(String carTrackerId, String roadType, Date date);

    List<CarTrackerRule> getCarTrackerRulesForDay(String carTrackerId, Date date);

    List<CarTrackerRule> getAmountOfRulesByCarTrackerId(String carTrackerId, int amount);

    String getCarTrackerIdByRuleId(long id);
}