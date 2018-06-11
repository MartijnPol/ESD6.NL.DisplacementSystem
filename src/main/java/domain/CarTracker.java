package domain;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Entity
@NamedQueries({
        @NamedQuery(name = "carTracker.findAllMovementsWithinPeriodByTrackerId", query = "SELECT cr FROM CarTrackerRule cr " +
                "WHERE cr.date BETWEEN :startDate AND :endDate AND cr.carTracker.id = :trackerId"),
        @NamedQuery(name = "carTracker.findById", query = "SELECT c FROM CarTracker c WHERE c.id = :id")
})
public class CarTracker implements Serializable {

    @Id
    private String id;

    private Long totalRules;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    @NotNull
    private String manufacturer;

    @NotNull
    private boolean enabled;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "carTracker")
    private List<CarTrackerRule> rules;

    public CarTracker() {
        rules = new ArrayList<>();
    }

    public CarTracker(Long totalRules, List<CarTrackerRule> rules) {
        this();
        this.totalRules = totalRules;
        this.rules = rules;
    }

    public void setRules(List<CarTrackerRule> rules) {
        this.rules = rules;
        calculateTotalRules();
    }

    public void addRules(List<CarTrackerRule> rules) {
        this.rules.addAll(rules);
        calculateTotalRules();
    }

    public void addRule(CarTrackerRule rule) {
        this.rules.add(rule);
        calculateTotalRules();
    }

    private void calculateTotalRules() {
        this.totalRules = (long) rules.size();
    }

    public JsonObject toJson() {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        Locale location = new Locale.Builder().setLanguage("nl").setRegion("NL").build();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", location);
        String formattedStartDate = dateFormat.format(this.startDate);
        for (CarTrackerRule carTrackerRule : rules) {
            jsonArrayBuilder.add(carTrackerRule.toJson());
        }

        return Json.createObjectBuilder()
                .add("id", this.id)
                .add("totalRules", this.totalRules)
                .add("startDate", formattedStartDate)
                .add("manufacturer", this.manufacturer)
                .add("CarTrackerRules", jsonArrayBuilder)
                .build();
    }

    @Override
    public String toString() {
        return "Tracker [id=" + id + ", TotalRules=" + totalRules + ", rules=" + rules + "]";
    }

    //<editor-fold defaultstate="collapsed" desc="Getters/Setters">
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTotalRules() {
        return totalRules;
    }

    public void setTotalRules(Long totalRules) {
        this.totalRules = totalRules;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<CarTrackerRule> getRules() {
        return rules;
    }

    //</editor-fold>
}
