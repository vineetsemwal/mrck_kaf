package com.example.demo.deliveryms;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

// Serde Serializer-Deserializer
public class DeliveriesStreamProcessing {
    private static final Logger Log = LoggerFactory.getLogger(DeliveriesStreamProcessing.class);

    private static boolean stop = false;

    public static void main(String[] args) throws Exception {

        Properties properties = KafkaPropertiesReader.read("application.properties");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, DeliveryMessageSerde.class);
        StreamsBuilder builder = new StreamsBuilder();
        //source processor giving us stream from topic input-words
        KStream<String, DeliveryMessage> inputStream = builder.stream("deliveries", Consumed.with(Serdes.String(),new DeliveryMessageSerde() ));

        KStream<String,DeliveryMessage>delivered=inputStream.filter((key,msg)->msg.getStatus()==DeliveryStatus.DELIVERED);
        delivered.peek((key,msg)->Log.info("***before sinking to delivered topic key="+key+"msg="+msg))
        .to("delivered");

       KStream<String,DeliveryMessage>dispatched =inputStream.filter((key,msg)->msg.getStatus()==DeliveryStatus.DISPATCHED);
                       dispatched.peek((key,msg)->Log.info("***before sinking to dispatched topic key="+key+"msg="+msg))
                       .to("dispatched");

        Topology topology = builder.build();
        Log.info("topology" + topology.describe());

        try (KafkaStreams kafkaStreams = new KafkaStreams(topology, properties)) {
            kafkaStreams.start();
            //  while (stop) {
            Thread.sleep(Long.MAX_VALUE);
            // }
            kafkaStreams.close();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Log.info("*** application closed");

        }));

    }

    static boolean isNumber(String text) {
        try {
            int number = Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
