package domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;

@JsonPropertyOrder({
        "identificationNumber",
        "startDate",
        "endDate"
})

/**
 * Created by Martijn van der Pol on 13-03-18
 **/
public class CarTrackerDataQuery {

    @JsonProperty("identificationNumber")
    private Long identificationNumber;

    @JsonProperty("startDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date startDate;

    @JsonProperty("endDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date endDate;

    public CarTrackerDataQuery() {
    }

    @JsonProperty("identificationNumber")
    public Long getIdentificationNumber() {
        return identificationNumber;
    }

    @JsonProperty("identificationNumber")
    public void setIdentificationNumber(Long identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    @JsonProperty("startDate")
    public Date getStartDate() {
        return startDate;
    }

    @JsonProperty("startDate")
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @JsonProperty("endDate")
    public Date getEndDate() {
        return endDate;
    }

    @JsonProperty("endDate")
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}