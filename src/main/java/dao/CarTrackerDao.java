package dao;

import domain.CarTracker;
import domain.CarTrackerDataQuery;

import java.util.Date;
import java.util.List;

public interface CarTrackerDao extends GenericDao<CarTracker> {

    CarTracker getRulesWithinPeriod(Long id, Date from, Date to);

    List<CarTracker> getRulesWithinMultiplePeriods(CarTrackerDataQuery[] carTrackerDataQueries);

    long getLastRuleId();

}
