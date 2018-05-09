package boundary.rest;

import domain.CarTracker;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;

/**
 * @author Thom van de Pas on 9-5-2018
 */
@RunWith(Arquillian.class)
public class CarTrackerArquillianTest {

    @ArquillianResource
    private URL deploymentURL;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackage(CarTracker.class.getPackage())
                .addClasses(CarTrackerResource.class);
    }

    @Test
    public void getAllCarTrackers(@ArquillianResteasyResource("api/CarTrackers") CarTrackerResource carTrackerResource) {

    }
}
