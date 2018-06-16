package domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(name = "carTrackerRule.getHighestRuleIdFromCarTrackerRules",
                query = "SELECT MAX(c.id) FROM CarTrackerRule c WHERE c.carTracker = :carTracker"),
        @NamedQuery(name = "carTrackerRule.getRulesByIDMonthAndYear",
                query = "SELECT c FROM CarTrackerRule c where c.carTracker.id = :carTrackerId and function('MONTH', c.date) = :month and function('YEAR', c.date) = :year"),
        @NamedQuery(name = "carTrackerRule.getCarTrackerRulesForDayAndRoadType",
                query = "SELECT c FROM CarTrackerRule c WHERE c.carTracker.id = :carTrackerId AND c.roadType = :roadType AND c.date = :date"),
        @NamedQuery(name = "carTrackerRule.getCarTrackerRulesForDay",
                query = "SELECT c FROM CarTrackerRule c WHERE c.carTracker.id = :carTrackerId AND c.date = :date")
})
public class CarTrackerRule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private CarTracker carTracker;

    private Long metersDriven;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss", timezone = "GMT")
    private Date date;

    private double lat;

    private double lon;

    private String roadType;

    public CarTrackerRule() {
        this.roadType = "O";
    }

    public CarTrackerRule(CarTracker carTracker, Long metersDriven, Date date, double lat, double lon) {
        this();
        this.carTracker = carTracker;
        this.metersDriven = metersDriven;
        this.date = date;
        this.lat = lat;
        this.lon = lon;
    }

    public JsonObject toJson() {
        Locale location = new Locale.Builder().setLanguage("nl").setRegion("NL").build();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", location);
        String date = dateFormat.format(this.date);
        return Json.createObjectBuilder()
                .add("id", this.id)
                .add("metersDriven", this.metersDriven)
                .add("date", date)
                .add("lat", this.lat)
                .add("lon", this.lon)
                .add("roadType", this.roadType == null ? "O" : this.roadType)
                .build();
    }

    //<editor-fold desc="Getters/Setters">
    public Long getId() {
        return id;
    }

    public void setId(Long ruleId) {
        this.id = ruleId;
    }

    public CarTracker getCarTracker() {
        return carTracker;
    }

    public void setCarTracker(CarTracker carTracker) {
        this.carTracker = carTracker;
    }

    public Long getMetersDriven() {
        return metersDriven;
    }

    public void setMetersDriven(Long kmDriven) {
        this.metersDriven = kmDriven;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getRoadType() {
        return roadType;
    }

    public void setRoadType(String roadType) {
        this.roadType = roadType;
    }

    //</editor-fold>

    //<editor-fold desc="equals/hashCode">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarTrackerRule that = (CarTrackerRule) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    //</editor-fold>
}
