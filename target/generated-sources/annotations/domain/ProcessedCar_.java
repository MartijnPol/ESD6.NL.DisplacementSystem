package domain;

import domain.CarTracker;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-06-13T12:55:25")
@StaticMetamodel(ProcessedCar.class)
public class ProcessedCar_ { 

    public static volatile SingularAttribute<ProcessedCar, CarTracker> carTracker;
    public static volatile SingularAttribute<ProcessedCar, Date> date;
    public static volatile SingularAttribute<ProcessedCar, Boolean> processed;
    public static volatile SingularAttribute<ProcessedCar, Long> id;

}