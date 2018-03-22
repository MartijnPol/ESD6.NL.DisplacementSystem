package service;

import dao.CarTrackerDao;
import dao.CarTrackerRuleDao;
import dao.JPADisplacementSystem;
import dao.ProcessedCarsDao;
import domain.CarTracker;
import domain.CarTrackerDataQuery;
import domain.CarTrackerRule;
import domain.ProcessedCars;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class CarTrackerService {

    @Inject
    @JPADisplacementSystem
    public CarTrackerDao carTrackerDao;

    @Inject
    @JPADisplacementSystem
    public CarTrackerRuleDao carTrackerRuleDao;

    @Inject
    @JPADisplacementSystem
    public ProcessedCarsDao processedCarsDao;

    @Inject
    public CarTrackerQueue carTrackerQueue;

    /**
     * Empty constructor
     */
    public CarTrackerService() {
    }

    /**
     * Function to save a given CarTracker object in the database
     *
     * @param carTracker is the CarTracker object that needs to be saved
     */
    public void create(CarTracker carTracker) {
        carTrackerDao.create(carTracker);
    }

    /**
     * Function to find CarTracker data by it's Id
     *
     * @param id is the given Id that belongs to a CarTracker data object
     * @return the found object belonging to the given Id
     */
    public CarTracker findById(Long id) {
        return this.carTrackerDao.findById(id);
    }

    /**
     * Function to get all CarTrackerData stored in the database
     *
     * @return A list of all CarTrackerData stored in the database
     */
    public List<CarTracker> getCarTrackers() {
        return carTrackerDao.findAll();
    }

    /**
     * Function to get all CarTrackerData within a given period
     *
     * @return a list of found CarTrackerData
     */
    public CarTracker getRulesWithinPeriod(Long identificationNumber, Date start, Date end) {
        return carTrackerDao.getRulesWithinPeriod(identificationNumber, start, end);
    }

    /**
     * Function to get all CarTrackerData within a given period
     *
     * @return a list of found CarTrackerData
     */
    public List<CarTracker> getRulesWithinMultiplePeriods(CarTrackerDataQuery[] carTrackerDataQueries) {
        return carTrackerDao.getRulesWithinMultiplePeriods(carTrackerDataQueries);
    }

    /**
     * Function to check if a given CarTrackerData object is 'safe'
     *
     * @param carTracker is the given carTrackerData object that needs to be checked
     */
    public void runAllChecks(CarTracker carTracker) {
        boolean storedDataCheck = this.storedDataCheck(carTracker);
        boolean idCheck = this.idCheck(carTracker);
        boolean sizeCheck = this.sizeCheck(carTracker);
        boolean valueCheck = this.missingRuleValuesCheck(carTracker);

        if (idCheck && sizeCheck && valueCheck && storedDataCheck) {
            this.carTrackerQueue.addQueue(carTracker);
        } else {
            this.processedCarsDao.create(new ProcessedCars(carTracker, new Date(), false));
        }
    }

    /**
     * TODO: Marc, even invullen s.v.p.
     *
     * @param carTracker
     * @return
     */
    public boolean missingRuleValuesCheck(CarTracker carTracker) {

        long i = carTracker.getRules().get(0).getId();
        for (CarTrackerRule carTrackerRule : carTracker.getRules()) {
            if (carTrackerRule.getId() != i) {
                System.out.println("CarTrackerID:" + " " + carTracker.getId() + " " + "RuleID:" + " " + carTrackerRule.getId() + " " + "isn't equal with count");
                return false;
            }
            if (carTrackerRule.getKmDriven().equals(null)) {
                System.out.println("CarTrackerID:" + " " + carTracker.getId() + " " + "RuleID:" + " " + carTrackerRule.getId() + " " + "has null at KmDriven");
                return false;
            }
            if (carTrackerRule.getDate().equals(null)) {
                System.out.println("CarTrackerID:" + " " + carTracker.getId() + " " + "RuleID:" + " " + carTrackerRule.getId() + " " + "has null at Date");
                return false;
            }
            if (carTrackerRule.getLat() == 0) {
                System.out.println("CarTrackerID:" + " " + carTracker.getId() + " " + "RuleID:" + " " + carTrackerRule.getId() + " " + "has 0 at lat");
                return false;
            }
            if (carTrackerRule.getLon() == 0) {
                System.out.println("CarTrackerID:" + " " + carTracker.getId() + " " + "RuleID:" + " " + carTrackerRule.getId() + " " + "has 0 at lon");
                return false;
            }
            i++;
        }
        return true;
    }

    /**
     * TODO: Marc, even invullen s.v.p.
     *
     * @param carTracker
     * @return a boolean if the size of the size ??
     */
    public boolean sizeCheck(CarTracker carTracker) {

        BigDecimal expectedRuleSize = new BigDecimal(carTracker.getRules().size());
        return new BigDecimal(carTracker.getTotalRules()).compareTo(expectedRuleSize) == 0;
    }

    /**
     * TODO: Marc
     *
     * @param carTracker
     * @return
     */
    public boolean idCheck(CarTracker carTracker) {
        Long highestKnownRuleId = this.carTrackerRuleDao.getHighestRuleIdFromCarTrackerRules(carTracker);

        if (carTracker.getRules().get(0).getId().equals(highestKnownRuleId + 1)) {
            return true;
        }

        System.out.println("CarTrackerId: " + carTracker.getId() + " New CarTrackerRuleId: " + carTracker.getRules().get(0).getId() + " doesn't match last database CarTrackerRuleId + 1: " + (highestKnownRuleId + 1));
        return false;
    }

    /**
     * TODO: Marc
     *
     * @param carTracker
     * @return
     */
    public boolean storedDataCheck(CarTracker carTracker) {
        List<ProcessedCars> cars = this.processedCarsDao.getNotProcessedDataById(carTracker.getId());

        return cars.isEmpty();
    }

    public List<JsonObject> replaceAllToJson(List<CarTracker> carTrackers) {
        List<JsonObject> jsonObjects = new ArrayList<JsonObject>();
        for (CarTracker carTracker : carTrackers) {
            jsonObjects.add(carTracker.toJson());
        }
        return jsonObjects;
    }
}
