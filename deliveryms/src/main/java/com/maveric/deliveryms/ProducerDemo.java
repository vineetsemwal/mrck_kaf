package com.maveric.deliveryms;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.Future;

public class ProducerDemo {
    private static final Logger Log = LoggerFactory.getLogger(ProducerDemo.class);

    public static void main(String[] args) throws Exception {
        System.out.println("****starting kafka client");
        Properties properties = KafkaPropertiesReader.read("remote_producer.properties");
      Log.info("*****properties"+properties);
        try(
        KafkaProducer<String, DeliveryMessage> producer = new KafkaProducer<>(properties);
        ) {
            String orderID = "order5";
        /*
        Future<RecordMetadata> future = producer.send(record);
        RecordMetadata metaData = future.get();
        System.out.println("record details topic=" + metaData.topic() + "-partition=" + metaData.partition() + "-offset=" + metaData.offset());
*/


            for (DeliveryStatus status : DeliveryStatus.values()) {
                DeliveryMessage msg = new DeliveryMessage(orderID, status);
                ProducerRecord<String, DeliveryMessage> record = new ProducerRecord<>("deliveries2", orderID, msg);
                producer.send(record, (metaData, exception) -> {
                    if (exception != null) {
                        Log.error("exception in sending message-getting metadata", exception);
                        return;
                    }
                    Log.info("record details topic=" + metaData.topic() + "-" + metaData.partition() + "-" + metaData.offset());

                });
            }

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                Log.info("****producer exiting");
            }));
        }
        Log.info("bye");
    }
}

