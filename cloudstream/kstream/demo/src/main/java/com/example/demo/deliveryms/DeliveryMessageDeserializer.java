package com.example.demo.deliveryms;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

public class DeliveryMessageDeserializer implements Deserializer<DeliveryMessage> {
  private static final ObjectMapper mapper=new ObjectMapper();
    @Override
    public DeliveryMessage deserialize(String topic, byte[] data) {
        try {
            DeliveryMessage msg = mapper.readValue(data, DeliveryMessage.class);
            return msg;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
