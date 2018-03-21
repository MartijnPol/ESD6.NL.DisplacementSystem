package domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "identificationNumber",
        "fromDate",
        "toDate"
})

/**
 * Created by Martijn van der Pol on 13-03-18
 **/
public class CarTrackerDataResponse {

    @JsonProperty("identificationNumber")
    private Long identificationNumber;

    @JsonProperty("fromDate")
    private Date fromDate;

    @JsonProperty("toDate")
    private Date toDate;

    @JsonProperty("carTrackerData")
    private List<CarTracker> carTrackerData;

    public CarTrackerDataResponse() {
    }

    public CarTrackerDataResponse(Long identificationNumber, Date fromDate, Date toDate) {
        this.identificationNumber = identificationNumber;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @JsonProperty("identificationNumber")
    public Long getIdentificationNumber() {
        return identificationNumber;
    }

    @JsonProperty("fromDate")
    public Date getFromDate() {
        return fromDate;
    }

    @JsonProperty("toDate")
    public Date getToDate() {
        return toDate;
    }

    @JsonProperty("carTrackerData")
    public List<CarTracker> getCarTrackerData() {
        return carTrackerData;
    }

    @JsonProperty("carTrackerData")
    public void setCarTrackerData(List<CarTracker> carTrackerData) {
        this.carTrackerData = carTrackerData;
    }

}