package com.example.demo;

import com.example.demo.deliveryms.DeliveryMessage;
import com.example.demo.deliveryms.DeliveryStatus;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;

@RequestMapping("/deliveries")
@RestController
public class DemoController {

    private StreamBridge bridge;
    public DemoController(StreamBridge  bridge){
        this.bridge=bridge;
    }

    @PostMapping("/add")
    public String sendDelivery(@RequestBody DeliveryMessageDTO requestData){
        DeliveryMessage msg=new DeliveryMessage(requestData.getOrderID(), DeliveryStatus.valueOf(requestData.getStatus()));
        System.out.println("msg="+msg);
       Message<DeliveryMessage>message= MessageBuilder.withPayload(msg)
                .setHeader(KafkaHeaders.KEY,msg.getOrderID().getBytes())
                        .build();

        bridge.send("deliveries-out-0",message);
        return "message sent";
    }



}
