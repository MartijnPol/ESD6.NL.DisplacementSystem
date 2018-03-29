/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jms;

import domain.CarTracker;

import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;


@Stateful
public class MessageProducer {

    @Resource(mappedName = "jms/GlassFishQueue")
    private Queue queue;

    @Resource(mappedName = "jms/GlassFishConnectionFactory")
    private ConnectionFactory cf;

    public MessageProducer() {
    }
    
    public void sentMessage(CarTracker carTracker){
            JMSContext context = cf.createContext();
            context.createProducer().send(queue, carTracker);
            context.close();
    }
}
