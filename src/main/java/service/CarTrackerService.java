package service;

import dao.CarTrackerDao;
import dao.CarTrackerRuleDao;
import dao.JPA;
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
    @JPA
    public CarTrackerDao carTrackerDao;

    @Inject
    @JPA
    public CarTrackerRuleDao carTrackerRuleDao;

    @Inject
    @JPA
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
     * Function to update a given CarTracker object in the database
     *
     * @param carTracker is the CarTracker object that needs to be updated
     */
    public void update(CarTracker carTracker) {
        carTrackerDao.update(carTracker);
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
     * Replaces all the CarTrackers to JsonObjects.
     *
     * @param carTrackers are the CarTrackers that need to be replaced.
     * @returns the List of JsonObjects.
     */
    public List<JsonObject> replaceAllToJson(List<CarTracker> carTrackers) {
        List<JsonObject> jsonObjects = new ArrayList<JsonObject>();
        for (CarTracker carTracker : carTrackers) {
            jsonObjects.add(carTracker.toJson());
        }
        return jsonObjects;
    }

    //<editor-fold desc="CarTracker validation">

    /**
     * Function to check if a given CarTrackerData object is 'safe'
     *
     * @param carTracker is the given carTrackerData object that needs to be checked
     */
    public void runAllChecks(CarTracker carTracker) {
        if (carTracker != null) {
            boolean storedDataCheck = this.storedDataCheck(carTracker.getId());
            boolean idCheck = this.idCheck(carTracker);
            boolean sizeCheck = this.sizeCheck(carTracker);
            boolean valueCheck = this.missingRuleValuesCheck(carTracker);

            if (idCheck && sizeCheck && valueCheck && storedDataCheck) {
                CarTracker foundCarTracker = carTrackerDao.findById(carTracker.getId());
                List<CarTrackerRule> receivedRules = carTracker.getRules();

                for (CarTrackerRule receivedRule : receivedRules) {
                    receivedRule.setCarTracker(carTracker);
                    carTrackerRuleDao.create(receivedRule);
                }

                foundCarTracker.addRules(receivedRules);

                carTrackerDao.update(foundCarTracker);
                processedCarsDao.create(new ProcessedCars(carTracker, new Date(), true));
            } else {
                this.processedCarsDao.create(new ProcessedCars(carTracker, new Date(), false));
            }
        }
    }

    /**
     * This method checks whether there are missing value in the CarTrackers' CarTrackerRules.
     *
     * @param carTracker is the CarTracker from which the CarTrackerRules are checked.
     * @returns true when the CarTrackerRules are valid, false if not.
     */
    public boolean missingRuleValuesCheck(CarTracker carTracker) {

        long i = carTracker.getRules().get(0).getId();
        for (CarTrackerRule carTrackerRule : carTracker.getRules()) {
            if (carTrackerRule.getId() != i) {
                System.out.println("CarTrackerID:" + " " + carTracker.getId() + " " + "RuleID:" + " "
                        + carTrackerRule.getId() + " " + "isn't equal with count");
                return false;
            }
            if (carTrackerRule.getKmDriven().equals(null)) {
                System.out.println("CarTrackerID:" + " " + carTracker.getId() + " " + "RuleID:" + " "
                        + carTrackerRule.getId() + " " + "has null at KmDriven");
                return false;
            }
            if (carTrackerRule.getDate().equals(null)) {
                System.out.println("CarTrackerID:" + " " + carTracker.getId() + " " + "RuleID:" + " "
                        + carTrackerRule.getId() + " " + "has null at Date");
                return false;
            }
            if (carTrackerRule.getLat() == 0) {
                System.out.println("CarTrackerID:" + " " + carTracker.getId() + " " + "RuleID:" + " "
                        + carTrackerRule.getId() + " " + "has 0 at lat");
                return false;
            }
            if (carTrackerRule.getLon() == 0) {
                System.out.println("CarTrackerID:" + " " + carTracker.getId() + " " + "RuleID:" + " "
                        + carTrackerRule.getId() + " " + "has 0 at lon");
                return false;
            }
            i++;
        }
        return true;
    }

    /**
     * Method that checks whether the size of the rules equals the TotalRules (field) of the CarTracker.
     *
     * @param carTracker the CarTracker which gets its Rules size checked.
     * @returns true if the size of the Rules is equal to the expectedRuleSize. False if not.
     */
    private boolean sizeCheck(CarTracker carTracker) {

        BigDecimal expectedRuleSize = new BigDecimal(carTracker.getRules().size());
        return new BigDecimal(carTracker.getTotalRules()).compareTo(expectedRuleSize) == 0;
    }

    /**
     * Checks whether the id of the last known CarTrackerRule +1 equals the newest CarTrackerRule id.
     * So we know there are no missing id's (they count up).
     *
     * @param carTracker the CarTracker which gets is CarTrackerRule id's checked and validated.
     * @returns true if they match. False if they don't match.
     */
    private boolean idCheck(CarTracker carTracker) {
        Long highestKnownRuleId = this.carTrackerRuleDao.getHighestRuleIdFromCarTrackerRules(carTracker);

        if (carTracker.getRules().get(0).getId().equals(highestKnownRuleId + 1)) {
            return true;
        }

        System.out.println("CarTrackerId: " + carTracker.getId() + " New CarTrackerRuleId: " + carTracker.getRules().get(0).getId() + " doesn't match last database CarTrackerRuleId + 1: " + (highestKnownRuleId + 1));
        return false;
    }

    /**
     * Retrieves a List of unprocessed Cars.
     *
     * @param carTrackerId is the id of the CarTracker that needs its processedCars checked.
     * @returns true if there are no unprocessed cars.
     */
    private boolean storedDataCheck(Long carTrackerId) {
        List<ProcessedCars> cars = this.processedCarsDao.getNotProcessedDataById(carTrackerId);

        return cars.isEmpty();
    }

    public void setCarTrackerDao(CarTrackerDao carTrackerDao) {
        this.carTrackerDao = carTrackerDao;
    }
}
