package dao.jpa;

import dao.CarTrackerRuleDao;
import dao.JPA;
import domain.CarTracker;
import domain.CarTrackerRule;

import javax.ejb.Stateless;
import java.util.Date;
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

    @Override
    public List<CarTrackerRule> getCarTrackerRulesForDayAndRoadType(String carTrackerId, String roadType, Date date) {
        return getEntityManager().createNamedQuery("carTrackerRule.getCarTrackerRulesForDayAndRoadType")
                .setParameter("carTrackerId", carTrackerId)
                .setParameter("roadType", roadType)
                .setParameter("date", date)
                .getResultList();
    }

    @Override
    public List<CarTrackerRule> getCarTrackerRulesForDay(String carTrackerId, Date date) {
        return getEntityManager().createNamedQuery("carTrackerRule.getCarTrackerRulesForDay")
                .setParameter("carTrackerId", carTrackerId)
                .setParameter("date", date)
                .getResultList();
    }

    @Override
    public List<CarTrackerRule> getAmountOfRulesByCarTrackerId(String carTrackerId, int amount) {
        return this.entityManager.createNamedQuery("carTrackerRule.getAmountOfRulesByCarTrackerId")
                .setParameter("carTrackerId", carTrackerId).setMaxResults(amount).getResultList();
    }

    @Override
    public String getCarTrackerIdByRuleId(long id) {
        return this.entityManager.createNamedQuery("carTrackerRule.getCarTrackerIdByRuleId", String.class)
                .setParameter("carTrackerRuleId", id)
                .getSingleResult();
    }
}
