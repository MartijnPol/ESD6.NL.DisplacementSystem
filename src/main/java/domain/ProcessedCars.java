package domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@NamedQueries({
        @NamedQuery(name = "processedCars.getNotProcessedDataById", query = "SELECT p FROM ProcessedCars p WHERE p.processed = false AND p.id= :id")

})
public class ProcessedCars implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private CarTracker carTracker;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy@HH:mm:ss")
    private Date date;

    private boolean processed;

    public ProcessedCars(CarTracker carTracker, Date date, boolean processed) {
        this.carTracker = carTracker;
        this.date = date;
        this.processed = processed;
    }

    public ProcessedCars() {
    }
}
