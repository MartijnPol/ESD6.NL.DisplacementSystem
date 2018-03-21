package dao.jpa;

import dao.CarTrackerDao;
import dao.JPADisplacementSystem;
import domain.CarTracker;
import domain.CarTrackerDataQuery;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
@JPADisplacementSystem
public class CarTrackerDaoJPAImpl extends GenericDaoJPAImpl<CarTracker> implements CarTrackerDao {

    @Override
    public CarTracker getRulesWithinPeriod(Long trackerId, Date startDate, Date endDate) {
        List rules = this.entityManager.createNamedQuery("carTracker.findAllMovementsWithinPeriodByTrackerId")
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setParameter("trackerId", trackerId).getResultList();

        CarTracker carTracker = new CarTracker();
        carTracker.setId(trackerId);
        carTracker.setRules(rules);
        return carTracker;
    }

    @Override
    public List<CarTracker> getRulesWithinMultiplePeriods(CarTrackerDataQuery[] carTrackerDataQueries) {
        List<CarTracker> carTrackerList = new ArrayList<>();
        for (CarTrackerDataQuery query : carTrackerDataQueries) {
            carTrackerList.add(getRulesWithinPeriod(query.getIdentificationNumber(), query.getStartDate(), query.getEndDate()));
        }
        return carTrackerList;
    }

    @Override
    public long getLastRuleId() {
        throw new NotImplementedException();
    }

}
