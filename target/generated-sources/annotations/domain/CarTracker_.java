package domain;

import domain.CarTrackerRule;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-05-07T12:43:01")
@StaticMetamodel(CarTracker.class)
public class CarTracker_ { 

    public static volatile ListAttribute<CarTracker, CarTrackerRule> rules;
    public static volatile SingularAttribute<CarTracker, Long> id;
    public static volatile SingularAttribute<CarTracker, Long> totalRules;

}