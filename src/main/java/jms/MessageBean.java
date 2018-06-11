package jms;

import com.mashape.unirest.http.exceptions.UnirestException;
import domain.CarTracker;
import service.CarTrackerService;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.io.UnsupportedEncodingException;
import java.time.Instant;

@MessageDriven(mappedName = "jms/GlassFishQueue", activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})

@Stateless
public class MessageBean implements MessageListener {

    @Inject
    private CarTrackerService cartrackerService;

    private int i = 0;

    public MessageBean() {
    }

    @Override
    public void onMessage(Message message) {
        ObjectMessage msg = (ObjectMessage) message;
        CarTracker carTracker;
        try {
            carTracker = (CarTracker) msg.getObject();
            this.i++;
            System.out.println(i + " " + "TEST" + " " + carTracker.toString());
            long startTime = Instant.now().getEpochSecond();
            try {
                this.cartrackerService.processCarTracker(carTracker);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            long endTime = Instant.now().getEpochSecond();

            long duration = (endTime - startTime);
            System.out.println("The time that processing the data of: " + carTracker.getId() +  " = " + duration);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
