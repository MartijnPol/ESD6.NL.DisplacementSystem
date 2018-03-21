package boundary.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Created by Martijn van der Pol on 14-03-18
 **/
@ApplicationPath("/api")
public class ApplicationConfig extends Application {

    public ApplicationConfig() {
    }
}
