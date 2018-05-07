package domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@NamedQueries({
        @NamedQuery(name = "processedCars.getNotProcessedDataById",
                query = "SELECT p FROM ProcessedCars p WHERE p.processed = false AND p.carTracker.id = :id")

})
public class ProcessedCars implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private CarTracker carTracker;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy@HH:mm:ss")
    private Date date;

    private boolean processed;

    public ProcessedCars() {
    }

    public ProcessedCars(CarTracker carTracker, Date date, boolean processed) {
        this.carTracker = carTracker;
        this.date = date;
        this.processed = processed;
    }

    public CarTracker getCarTracker() {
        return carTracker;
    }

    public void setCarTracker(CarTracker carTracker) {
        this.carTracker = carTracker;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
