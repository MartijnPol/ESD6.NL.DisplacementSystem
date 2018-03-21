package domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@XmlRootElement
@NamedQuery(name = "carTracker.findAllMovementsWithinPeriodByTrackerId", query = "SELECT cr FROM CarTrackerRule cr " +
        "WHERE cr.date BETWEEN :startDate AND :endDate AND cr.carTracker.id = :trackerId")
public class CarTracker implements Serializable {

    @Id
    @GeneratedValue
    @JsonProperty
    private Long id;

    @JsonProperty
    private Long totalRules;

    @JsonProperty
    @OneToMany(cascade = CascadeType.ALL)
    private List<CarTrackerRule> rules;

    public CarTracker() {
        rules = new ArrayList<>();
    }

    public CarTracker(Long totalRules, List<CarTrackerRule> rules) {
        this();
        this.totalRules = totalRules;
        this.rules = rules;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTotalRules() {
        return totalRules;
    }

    public void setTotalRules(Long totalRules) {
        this.totalRules = totalRules;
    }

    public List<CarTrackerRule> getRules() {
        return rules;
    }

    public void setRules(List<CarTrackerRule> rules) {
        this.rules = rules;
    }

    public JsonObject toJson() {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for (CarTrackerRule carTrackerRule : rules) {
            jsonArrayBuilder.add(carTrackerRule.toJson());
        }

        return Json.createObjectBuilder()
                .add("CarTrackerId", this.id)
                .add("carTrackerRules", jsonArrayBuilder)
                .build();
    }

    @Override
    public String toString() {
        return "Tracker [id=" + id + ", TotalRules=" + totalRules + ", rules=" + rules + "]";
    }
}
