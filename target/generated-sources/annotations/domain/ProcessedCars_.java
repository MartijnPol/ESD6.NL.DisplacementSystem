package domain;

import domain.CarTracker;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-04-06T12:12:55")
@StaticMetamodel(ProcessedCars.class)
public class ProcessedCars_ { 

    public static volatile SingularAttribute<ProcessedCars, CarTracker> carTracker;
    public static volatile SingularAttribute<ProcessedCars, Date> date;
    public static volatile SingularAttribute<ProcessedCars, Boolean> processed;
    public static volatile SingularAttribute<ProcessedCars, Long> id;

}