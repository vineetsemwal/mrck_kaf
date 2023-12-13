package com.maveric.deliveryms;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

// Serde Serializer-Deserializer
public class StreamDemo3 {
    private static final Logger Log = LoggerFactory.getLogger(StreamDemo3.class);

    private static boolean stop = false;

    public static void main(String[] args) throws Exception {

        Properties properties = KafkaPropertiesReader.read("application.properties");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        StreamsBuilder builder = new StreamsBuilder();
        //source processor giving us stream from topic input-words
        KStream<String, String> inputStream = builder.stream("string-number", Consumed.with(Serdes.String(), Serdes.String()));

        KStream<String, String> numberStream = inputStream.filter((key, value) -> isNumber(value));
        KStream<String, Integer> mappedInteger = numberStream.mapValues((key, value) -> Integer.parseInt(value));
        mappedInteger
                .peek((key, value) -> System.out.println("***before sinking to numbers topic key=" + key + "-value=" + value))
                .to("numbers", Produced.with(Serdes.String(), Serdes.Integer()));

        KStream<String, String> textStream = inputStream.filter((key, value) -> !isNumber(value))
                .peek((key, value) -> System.out.println("***before sinking to strings topic key=" + key + "-value=" + value));
        textStream.to("strings", Produced.with(Serdes.String(), Serdes.String()));


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
