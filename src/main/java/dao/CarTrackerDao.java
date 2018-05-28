package dao;

import domain.CarTracker;
import domain.CarTrackerDataQuery;

import java.util.Date;
import java.util.List;

public interface CarTrackerDao extends GenericDao<CarTracker> {

    CarTracker findById(String id);

    CarTracker getRulesWithinPeriod(String id, Date from, Date to);

    List<CarTracker> getRulesWithinMultiplePeriods(CarTrackerDataQuery[] carTrackerDataQueries);
}
