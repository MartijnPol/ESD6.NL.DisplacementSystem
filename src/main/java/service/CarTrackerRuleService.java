package service;

import com.mysql.jdbc.StringUtils;
import dao.CarTrackerRuleDao;
import dao.JPA;
import domain.CarTracker;
import domain.CarTrackerRule;

import javax.inject.Inject;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Thom van de Pas on 16-6-2018
 */
public class CarTrackerRuleService {

    @Inject
    @JPA
    private CarTrackerRuleDao carTrackerRuleDao;


    public CarTrackerRuleService() {
    }

    public CarTrackerRule create(CarTrackerRule carTrackerRule) {
        return this.carTrackerRuleDao.create(carTrackerRule);
    }

    public CarTrackerRule update(CarTrackerRule carTrackerRule) {
        return this.carTrackerRuleDao.update(carTrackerRule);
    }

    public List<CarTrackerRule> getCarTrackerRulesForDayAndRoadType(String carTrackerId, String roadType, Date date) {
        if (!StringUtils.isNullOrEmpty(carTrackerId) && !StringUtils.isNullOrEmpty(roadType)) {
            return this.carTrackerRuleDao.getCarTrackerRulesForDayAndRoadType(carTrackerId, roadType, date);
        }
        return null;
    }

    public List<CarTrackerRule> getCarTrackerRulesForDay(String carTrackerId, Date date) {
        if (!StringUtils.isNullOrEmpty(carTrackerId)) {
            return this.carTrackerRuleDao.getCarTrackerRulesForDay(carTrackerId, date);
        }
        return null;
    }

    public List<CarTrackerRule> getAmountOfRulesByCarTrackerId(String carTrackerId, int amount) {
        if (!StringUtils.isNullOrEmpty(carTrackerId)) {
            return this.carTrackerRuleDao.getAmountOfRulesByCarTrackerId(carTrackerId, amount);
        }
        return null;
    }


    public String getCarTrackerIdByRuleId(long carTrackerRuleId) {
        if (carTrackerRuleId != 0L) {
            return this.carTrackerRuleDao.getCarTrackerIdByRuleId(carTrackerRuleId);
        }
        return null;
    }

    /**
     * Replaces all the CarTrackerRules to JsonObjects.
     *
     * @param carTrackerRules are the CarTrackerRules that need to be replaced.
     * @return the List of JsonObjects.
     */
    public List<JsonObject> replaceAllToJson(List<CarTrackerRule> carTrackerRules) {
        List<JsonObject> jsonObjects = new ArrayList<>();
        for (CarTrackerRule carTrackerRule : carTrackerRules) {
            jsonObjects.add(carTrackerRule.toJson());
        }
        return jsonObjects;
    }
}
