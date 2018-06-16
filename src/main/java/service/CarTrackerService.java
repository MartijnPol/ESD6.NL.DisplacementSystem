package service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import dao.CarTrackerDao;
import dao.CarTrackerRuleDao;
import dao.JPA;
import dao.ProcessedCarsDao;
import domain.CarTracker;
import domain.CarTrackerDataQuery;
import domain.CarTrackerRule;
import domain.ProcessedCar;
import org.json.JSONArray;
import org.json.JSONObject;
import util.StringHelper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

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
    public CarTracker create(CarTracker carTracker) {
        return this.carTrackerDao.create(carTracker);
    }

    /**
     * Function to update a given CarTracker object in the database
     *
     * @param carTracker is the CarTracker object that needs to be updated
     */
    public CarTracker update(CarTracker carTracker) {
        return this.carTrackerDao.update(carTracker);
    }

    /**
     * Function to find CarTracker data by it's Id
     *
     * @param id is the given Id that belongs to a CarTracker data object
     * @return the found object belonging to the given Id
     */
    public CarTracker findById(String id) {
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
    public CarTracker getRulesWithinPeriod(String identificationNumber, Date start, Date end) {
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
     * @return the List of JsonObjects.
     */
    public List<JsonObject> replaceAllToJson(List<CarTracker> carTrackers) {
        List<JsonObject> jsonObjects = new ArrayList<>();
        for (CarTracker carTracker : carTrackers) {
            jsonObjects.add(carTracker.toJson());
        }
        return jsonObjects;
    }

    //<editor-fold desc="Google Road API">

    /**
     * Retrieve places from the Google API.
     * Results are returned in a JSONArray, that later could be used for determining roads.
     * Latitude longitude path should be correctly formatted for example -35.27801,149.12958|-35.28032,149.12907
     * When the latitude longitude path is null or empty an empty JSONArray is returned.
     *
     * @param latLonPath
     * @return JSONArray containing places or an empty JSONArray when the path is null or empty.
     * @throws UnirestException             When something goes wrong when requesting the data.
     * @throws UnsupportedEncodingException When something goes wrong during encoding the URL.
     */
    public JSONArray getPlacesForLatLonPath(String latLonPath) throws UnirestException, UnsupportedEncodingException {
        if (!StringHelper.isEmpty(latLonPath)) {
            String encodedURL = URLEncoder.encode(latLonPath, "UTF-8");

            String herokuUrl = "http://viezehack-js.herokuapp.com/hack/roads?path=" + encodedURL;
            GetRequest getRequest = Unirest.get(herokuUrl);
            HttpResponse<JsonNode> jsonNodeHttpResponse = getRequest.asJson();
            JSONObject object = jsonNodeHttpResponse.getBody().getObject();

            return object.getJSONArray("snappedPoints");
        }

        return new JSONArray();
    }

    /**
     * Retrieve road names from the Google API.
     * Results are returned in a list, that later could be used for calculations purposes.
     * When there are no places available an empty list is returned
     *
     * @param placesForLatLonPath Array containing all places.
     * @return List of all road names when there are no places available an empty list is returned
     * @throws UnsupportedEncodingException When something goes wrong during encoding the URL.
     * @throws UnirestException             When something goes wrong when requesting the data.
     */
    public String getPlaceByLatAndLon(JSONArray placesForLatLonPath) throws UnsupportedEncodingException, UnirestException {

        JSONObject placeJSONObject = placesForLatLonPath.getJSONObject(0);
        String placeId = placeJSONObject.getString("placeId");

        String encodedURL = URLEncoder.encode(placeId, "UTF-8");
        String url = "http://viezehack-js.herokuapp.com/hack/places?id=" + encodedURL;

        JSONObject jsonResponseObject = Unirest.get(url).asJson().getBody().getObject();
        String roadType = jsonResponseObject.getJSONObject("result").getJSONArray("address_components").getJSONObject(0).getString("short_name").substring(0, 1);
        if (!roadType.equals("A") && !roadType.equals("N")) {
            return "O";
        } else if(roadType.equals("E")) {
            return "A";
        } else {
            return roadType;
        }
    }

    //</editor-fold>

    //<editor-fold desc="All the CarTracker checks">

    /**
     * Format latitude longitude values so they can be used by the Google Roads API.
     * For example -35.27801,149.12958|-35.28032,149.12907
     * When no rules are available an empty string will be returned.
     *
     * @param rules CarTrackerRules list containing the necessary
     * @return Formatted lat and lon path when no rules are available empty string is returned
     */
    public String getLatLonPath(List<CarTrackerRule> rules) {
        if (!rules.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();

            for (CarTrackerRule carTrackerRule : rules) {
                double lat = carTrackerRule.getLat();
                double lon = carTrackerRule.getLon();

                stringBuilder.append(lat + "," + lon + "|");
            }

            stringBuilder.setLength(stringBuilder.length() - 1);

            return stringBuilder.toString();
        }

        return "";
    }

    /**
     * Method to update a CarTracker after the checks are run.
     * First the CarTracker is searched in the DataBase and if
     *
     * @param carTracker is the CarTracker that will be updated.
     */
    public void processCarTracker(CarTracker carTracker) throws UnsupportedEncodingException, UnirestException {
        if (carTracker != null) {

            System.out.println("Processing CarTracker" + carTracker.getId());
            CarTracker foundCarTracker = this.findById(carTracker.getId());

            boolean safe = this.executeAllCarTrackerChecks(carTracker);

            if (safe) {
                if (foundCarTracker != null) {
                    Long highestKnownCarTrackerRuleId = this.carTrackerRuleDao.getHighestRuleIdFromCarTrackerRules(carTracker);
                    CarTrackerRule newestCarTrackerRule = this.carTrackerRuleDao.findById(highestKnownCarTrackerRuleId);
                    List<CarTrackerRule> carTrackerRuleList = Collections.singletonList(newestCarTrackerRule);
                    String latLonPath = getLatLonPath(carTrackerRuleList);
                    JSONArray placesForLatLonPath = getPlacesForLatLonPath(latLonPath);
                    String roadType = getPlaceByLatAndLon(placesForLatLonPath);

                    newestCarTrackerRule.setRoadType(roadType);
                    newestCarTrackerRule.setCarTracker(foundCarTracker);

                    this.update(foundCarTracker);

                    this.processedCarsDao.create(new ProcessedCar(foundCarTracker, new Date(), true));
                } else {
                    System.out.println("No CarTracker found with id: " + carTracker.getId());
                }
            } else {
                this.processedCarsDao.create(new ProcessedCar(foundCarTracker, new Date(), false));
            }
        }
    }

    /**
     * Function to run all the checks for the given CarTracker.
     * The function checks whether the CarTracker is 'safe'.
     *
     * @param carTracker is the given carTrackerData object that needs to be checked
     */
    public boolean executeAllCarTrackerChecks(CarTracker carTracker) {
        System.out.println("Running checks");

        boolean sizeCheck = this.sizeCheck(carTracker);
        boolean missingRuleValuesCheck = this.missingRuleValuesCheck(carTracker);
        boolean storedDataCheck = this.storedDataCheck(carTracker.getId());

        return sizeCheck && missingRuleValuesCheck && storedDataCheck;

    }

    /**
     * This method checks whether there are missing value in the CarTrackers' CarTrackerRules.
     *
     * @param carTracker is the CarTracker from which the CarTrackerRules are checked.
     * @return true when the CarTrackerRules are valid, false if not.
     */
    public boolean missingRuleValuesCheck(CarTracker carTracker) {

        for (CarTrackerRule carTrackerRule : carTracker.getRules()) {
            if (Objects.isNull(carTrackerRule.getMetersDriven())) {
                System.out.println("CarTrackerID:" + " " + carTracker.getId() + " " + "RuleID:" + " "
                        + carTrackerRule.getId() + " " + "has null at KmDriven");
                return false;
            }
            if (Objects.isNull(carTrackerRule.getDate())) {
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
        }
        return true;
    }

    /**
     * Method that checks whether the size of the rules equals the TotalRules (field) of the CarTracker.
     *
     * @param carTracker the CarTracker which gets its Rules size checked.
     * @return true if the size of the Rules is equal to the expectedRuleSize. False if not.
     */
    public boolean sizeCheck(CarTracker carTracker) {
        return carTracker.getRules().size() == carTracker.getTotalRules();
    }

    /**
     * Checks whether the id of the last known CarTrackerRule +1 equals the newest CarTrackerRule id.
     * So we know there are no missing id's (they count up).
     *
     * @param carTracker the CarTracker which gets is CarTrackerRule id's checked and validated.
     * @return true if they match. False if they don't match.
     */
    public boolean idCheck(CarTracker carTracker) {
        Long highestKnownRuleId = this.carTrackerRuleDao.getHighestRuleIdFromCarTrackerRules(carTracker);

        if (!carTracker.getRules().isEmpty()) {
            CarTrackerRule foundCarTrackerRule = carTracker.getRules().get(0);
            return foundCarTrackerRule.getId().equals(highestKnownRuleId + 1);
        }

        System.out.println("CarTrackerId: " + carTracker.getId() + " New CarTrackerRuleId doesn't match last database CarTrackerRuleId + 1: " + (highestKnownRuleId + 1));
        return false;
    }

    /**
     * Retrieves a List of unprocessed Cars.
     *
     * @param carTrackerId is the id of the CarTracker that needs its processedCars checked.
     * @return true if there are no unprocessed cars.
     */
    public boolean storedDataCheck(String carTrackerId) {
        return this.processedCarsDao.getNotProcessedDataById(carTrackerId).isEmpty();
    }
    //</editor-fold>

    public void setCarTrackerDao(CarTrackerDao carTrackerDao) {
        this.carTrackerDao = carTrackerDao;
    }

    public void setCarTrackerRuleDao(CarTrackerRuleDao carTrackerRuleDao) {
        this.carTrackerRuleDao = carTrackerRuleDao;
    }

    public void setProcessedCarsDao(ProcessedCarsDao processedCarsDao) {
        this.processedCarsDao = processedCarsDao;
    }
}
