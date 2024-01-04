package com.example.demo.deliveryms;

import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

public class DeliveryMessageDeserializer implements Deserializer<DeliveryMessage> {
    @Override
    public DeliveryMessage deserialize(String topic, byte[] data) {
        String text=new String(data, StandardCharsets.UTF_8);
        String parts[]=text.split("-");
        String orderId=parts[0];
        String statusText=parts[1];
        DeliveryStatus status=DeliveryStatus.valueOf(statusText);
        return new DeliveryMessage(orderId,status);
    }
}
