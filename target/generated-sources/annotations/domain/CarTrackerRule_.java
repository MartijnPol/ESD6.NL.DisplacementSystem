package domain;

import domain.CarTracker;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-06-16T12:49:11")
@StaticMetamodel(CarTrackerRule.class)
public class CarTrackerRule_ { 

    public static volatile SingularAttribute<CarTrackerRule, CarTracker> carTracker;
    public static volatile SingularAttribute<CarTrackerRule, Date> date;
    public static volatile SingularAttribute<CarTrackerRule, String> roadType;
    public static volatile SingularAttribute<CarTrackerRule, Long> metersDriven;
    public static volatile SingularAttribute<CarTrackerRule, Double> lon;
    public static volatile SingularAttribute<CarTrackerRule, Long> id;
    public static volatile SingularAttribute<CarTrackerRule, Double> lat;

}