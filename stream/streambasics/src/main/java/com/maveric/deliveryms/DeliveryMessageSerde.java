package com.maveric.deliveryms;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class DeliveryMessageSerde implements Serde<DeliveryMessage> {
    @Override
    public Serializer<DeliveryMessage> serializer() {
        return new DeliveryMessageSerializer();
    }

    @Override
    public Deserializer<DeliveryMessage> deserializer() {
        return new DeliveryMessageDeserializer();
    }
}
