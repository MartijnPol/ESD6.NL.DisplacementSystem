package dao.jpa;

import dao.CarTrackerRuleDao;
import dao.JPA;
import domain.CarTracker;
import domain.CarTrackerRule;

import javax.ejb.Stateless;

/**
 * @author Thom van de Pas on 22-3-2018
 */
@Stateless
@JPA
public class CarTrackerRuleJPAImpl extends GenericDaoJPAImpl<CarTrackerRule> implements CarTrackerRuleDao {

    @Override
    public Long getHighestRuleIdFromCarTrackerRules(CarTracker carTracker) {
        Long carTrackerRuleId = getEntityManager().createNamedQuery("carTrackerRule.getHighestRuleIdFromCarTrackerRules", Long.class)
                .setParameter("carTracker", carTracker)
                .getSingleResult();

        return carTrackerRuleId;
    }
}
