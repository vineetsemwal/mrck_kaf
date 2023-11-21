package com.example.demo;


import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.util.Map;

@Service
public class DeliveryService {

    @JmsListener(destination = "orders", containerFactory = "jmsContainerFactory")
    //@JmsListener(destination = "orders")
    public void consumer(Message<Map> message) throws JMSException {
        Map order=message.getPayload();
        System.out.println("order received="+order);
    }

}
