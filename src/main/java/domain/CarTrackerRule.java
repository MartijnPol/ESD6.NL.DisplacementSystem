package domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class CarTrackerRule implements Serializable {

    @Id
    private Long id;

    @ManyToOne
    @JsonIgnore
    private CarTracker carTracker;

    @JsonProperty
    private Long kmDriven;

    @JsonProperty
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy@HH:mm:ss")
    private Date date;

    @JsonProperty
    private double lat;

    @JsonProperty
    private double lon;

    @JsonProperty
    private boolean driven;

    public CarTrackerRule(Long ruleId, CarTracker carTracker, Long kmDriven, Date date, double lat, double lon, boolean driven) {
        this.id = ruleId;
        this.carTracker = carTracker;
        this.kmDriven = kmDriven;
        this.date = date;
        this.lat = lat;
        this.lon = lon;
        this.driven = driven;
    }

    public CarTrackerRule() {
    }

    public JsonObject toJson() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String date = dateFormat.format(this.date);
        return Json.createObjectBuilder()
                .add("id", this.id)
                .add("date", date)
                .add("latitude", this.lat)
                .add("longitude", this.lon)
                .add("hasDriven", driven)
                .build();
    }

    public Long getRuleId() {
        return id;
    }

    public void setRuleId(Long ruleId) {
        this.id = ruleId;
    }

    public CarTracker getCarTracker() {
        return carTracker;
    }

    public void setCarTracker(CarTracker carTracker) {
        this.carTracker = carTracker;
    }

    public Long getKmDriven() {
        return kmDriven;
    }

    public void setKmDriven(Long kmDriven) {
        this.kmDriven = kmDriven;
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

    public boolean isDriven() {
        return driven;
    }

    public void setDriven(boolean driven) {
        this.driven = driven;
    }

    @Override
    public String toString() {
        return " \n Rule [ruleId=" + id + ", carTrackerId=" + carTracker.getId() + ", km driven=" + kmDriven
                + ", date" + date + ", latitude=" + lat
                + ", longitude=" + lon + ", driven=" + driven + "]";
    }
}
