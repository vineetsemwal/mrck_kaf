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
public class StreamDemo2 {
    private static final Logger Log = LoggerFactory.getLogger(StreamDemo2.class);

    private static boolean stop = false;

    public static void main(String[] args) throws Exception {

        Properties properties = KafkaPropertiesReader.read("application.properties");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        StreamsBuilder builder = new StreamsBuilder();
        //source processor giving us stream from topic input-words
        KStream<String, String> inputStream = builder.stream("input-words", Consumed.with(Serdes.String(), Serdes.String()));
        //intermediate processor gives another stream
        //inputStream.peek((key,value)->Log.info("peeking key="+key+"value="+value))
        KStream<String, String> upperStream = inputStream.mapValues(value -> value.toUpperCase());
        //sink processor, sinking the events/messages to topic, terminal operation, does NOT give another stream
        upperStream.to("output-words", Produced.with(Serdes.String(), Serdes.String()));

        KStream<String, Integer> lengthStream = inputStream.mapValues(value -> value.length());
        lengthStream.to("words-length", Produced.with(Serdes.String(),Serdes.Integer() ));

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

}
