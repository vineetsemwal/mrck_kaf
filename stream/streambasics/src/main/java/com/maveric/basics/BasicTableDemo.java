package com.maveric.basics;

import com.maveric.deliveryms.KafkaPropertiesReader;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

// Serde Serializer-Deserializer
public class BasicTableDemo {
    private static final Logger Log = LoggerFactory.getLogger(BasicTableDemo.class);

    private static boolean stop = false;

    public static void main(String[] args) throws Exception {

        Properties properties = KafkaPropertiesReader.read("application.properties");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        StreamsBuilder builder = new StreamsBuilder();
        //source processor giving us stream from topic csv
        KTable<String, String> inputTable = builder.table("table-input",
                Consumed.with(Serdes.String(), Serdes.String()));
                Materialized.as("table-exp-store");
        inputTable.toStream()
                .peek((key,value)-> System.out.println("key="+key+"value="+value))
                .to("table-stream-output");

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
