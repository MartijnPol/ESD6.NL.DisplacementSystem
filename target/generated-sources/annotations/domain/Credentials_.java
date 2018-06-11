package domain;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-06-06T14:52:55")
@StaticMetamodel(Credentials.class)
public class Credentials_ { 

    public static volatile SingularAttribute<Credentials, Long> id;
    public static volatile SingularAttribute<Credentials, String> applicationName;
    public static volatile SingularAttribute<Credentials, Date> expirationDate;
    public static volatile SingularAttribute<Credentials, String> token;

}