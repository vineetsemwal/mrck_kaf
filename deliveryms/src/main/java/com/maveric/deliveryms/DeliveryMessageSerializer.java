package com.maveric.deliveryms;

import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DeliveryMessageSerializer implements Serializer<DeliveryMessage> {
    @Override
    public byte[] serialize(String topic, DeliveryMessage data) {
        String text=data.getOrderID()+"-"+data.getStatus();
        return text.getBytes(StandardCharsets.UTF_8);
    }
}
