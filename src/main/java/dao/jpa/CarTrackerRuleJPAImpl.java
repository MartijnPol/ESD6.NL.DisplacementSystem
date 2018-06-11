package dao.jpa;

import dao.CarTrackerRuleDao;
import dao.JPA;
import domain.CarTracker;
import domain.CarTrackerRule;

import javax.ejb.Stateless;
import java.util.List;

/**
 * @author Thom van de Pas on 22-3-2018
 */
@Stateless
@JPA
public class CarTrackerRuleJPAImpl extends GenericDaoJPAImpl<CarTrackerRule> implements CarTrackerRuleDao {

    @Override
    public Long getHighestRuleIdFromCarTrackerRules(CarTracker carTracker) {
        return getEntityManager().createNamedQuery("carTrackerRule.getHighestRuleIdFromCarTrackerRules", Long.class)
                .setParameter("carTracker", carTracker)
                .getSingleResult();
    }

    @Override
    public List<CarTrackerRule> getRulesByIDMonthAndYear(String id, int month, int year) {
        return getEntityManager().createNamedQuery("carTrackerRule.getRulesByIDMonthAndYear")
                .setParameter("carTrackerId", id)
                .setParameter("month", month)
                .setParameter("year", year)
                .getResultList();
    }
}
