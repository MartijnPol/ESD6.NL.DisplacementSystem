package domain;

import domain.CarTrackerRule;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-05-28T21:08:42")
@StaticMetamodel(CarTracker.class)
public class CarTracker_ { 

    public static volatile SingularAttribute<CarTracker, Date> endDate;
    public static volatile ListAttribute<CarTracker, CarTrackerRule> rules;
    public static volatile SingularAttribute<CarTracker, String> id;
    public static volatile SingularAttribute<CarTracker, Long> totalRules;
    public static volatile SingularAttribute<CarTracker, Date> startDate;
    public static volatile SingularAttribute<CarTracker, Boolean> enabled;
    public static volatile SingularAttribute<CarTracker, String> manufacturer;

}