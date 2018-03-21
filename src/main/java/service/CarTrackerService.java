package service;

import dao.CarTrackerDao;
import dao.JPADisplacementSystem;
import dao.ProcessedCarsDao;
import domain.CarTracker;
import domain.CarTrackerDataQuery;
import domain.CarTrackerRule;
import domain.ProcessedCars;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
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
     * @param carTrackerData is the given carTrackerData object that needs to be checked
     */
    public void runAllChecks(CarTracker carTrackerData) {
        boolean storedDataCheck = storedDataCheck(carTrackerData);
        boolean idCheck = idCheck(carTrackerData);
        boolean sizeCheck = sizeCheck(carTrackerData);
        boolean valueCheck = missingRuleValuesCheck(carTrackerData);

        if (idCheck && sizeCheck && valueCheck && storedDataCheck) {
            carTrackerQueue.addQueue(carTrackerData);
        } else {
            processedCarsDao.create(new ProcessedCars(carTrackerData, new Date(), false));
        }
    }

    /**
     * TODO: Marc, even invullen s.v.p.
     *
     * @param data
     * @return
     */
    public boolean missingRuleValuesCheck(CarTracker data) {

        long i = data.getRules().get(0).getRuleId();
        for (CarTrackerRule carTrackerRule : data.getRules()) {
            i++;

            if (carTrackerRule.getRuleId() != i) {
                System.out.println("CarTrackerID:" + " " + data.getId() + " " + "RuleID:" + " " + carTrackerRule.getRuleId() + " " + "isn't equal with count");
                return false;
            }
            if (carTrackerRule.getKmDriven().equals(null)) {
                System.out.println("CarTrackerID:" + " " + data.getId() + " " + "RuleID:" + " " + carTrackerRule.getRuleId() + " " + "has null at KmDriven");
                return false;
            }
            if (carTrackerRule.getDate().equals(null)) {
                System.out.println("CarTrackerID:" + " " + data.getId() + " " + "RuleID:" + " " + carTrackerRule.getRuleId() + " " + "has null at Date");
                return false;
            }
            if (carTrackerRule.getLat() == 0) {
                System.out.println("CarTrackerID:" + " " + data.getId() + " " + "RuleID:" + " " + carTrackerRule.getRuleId() + " " + "has 0 at lat");
                return false;
            }
            if (carTrackerRule.getLon() == 0) {
                System.out.println("CarTrackerID:" + " " + data.getId() + " " + "RuleID:" + " " + carTrackerRule.getRuleId() + " " + "has 0 at lon");
                return false;
            }
        }
        return true;
    }

    /**
     * TODO: Marc, even invullen s.v.p.
     *
     * @param data
     * @return a boolean if the size of the size ??
     */
    public boolean sizeCheck(CarTracker data) {

        if (data.getTotalRules().equals((data.getRules().size()))) {
            return true;
        }

        System.out.println("CarTrackerID:" + " " + data.getId() + " " + "Total number of rules doesn't match size of given rules");
        return false;
    }

    /**
     * TODO: Marc
     *
     * @param data
     * @return
     */
    public boolean idCheck(CarTracker data) {

        CarTracker carTracker = findById(data.getId());
        List<CarTrackerRule> rules = carTracker.getRules();
        long ruleID = rules.get(rules.size() - 1).getRuleId();
        //hier moet ik even een query van maken

        if (data.getRules().get(0).getRuleId().equals(ruleID + 1)) {
            return true;
        }

        System.out.println("CarTrackerID:" + " " + data.getId() + " " + "New RuleID:" + " " + data.getRules().get(0).getRuleId() + " " + "doesn't match last database RuleID + 1:" + " " + (ruleID + 1));
        return false;
    }


    /**
     * TODO: Marc
     *
     * @param data
     * @return
     */
    public boolean storedDataCheck(CarTracker data) {
        List<ProcessedCars> cars = processedCarsDao.getNotProcessedDataById(data.getId());

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
