package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.*;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class SenderController {
    private JmsTemplate jmsTemplate;

    @Autowired
    public SenderController(JmsTemplate template){
        this.jmsTemplate=template;
    }

    @PostMapping("/send")
    public String send(@RequestBody CreatedOrder order) throws  JMSException{
        System.out.println("sending");
        Map<String, Object> map=new HashMap();
        map.put("itemName",order.getItemName());
        map.put("orderId",order.getOrderId());
        map.put("price",order.getPrice());
        map.put("itemsUnit",order.getItemsUnit());
       Topic topic="orders"::toString;
     //  Topic topic=jmsTemplate.getConnectionFactory().createConnection()
      //        .createSession().createTopic("orders");
        jmsTemplate.convertAndSend(topic, map);
        return "order created";
    }
}
