package com.example.demo.deliveryms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;

public class DeliveryMessageSerializer implements Serializer<DeliveryMessage> {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, DeliveryMessage data) {
        try {
            byte bytes[] = mapper.writeValueAsBytes(data);
            return bytes;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
