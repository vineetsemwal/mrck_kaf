package com.maveric.basics;

import com.maveric.deliveryms.KafkaPropertiesReader;
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

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

// Serde Serializer-Deserializer
public class StreamDemo4 {
    private static final Logger Log = LoggerFactory.getLogger(StreamDemo4.class);

    private static boolean stop = false;

    public static void main(String[] args) throws Exception {

        Properties properties = KafkaPropertiesReader.read("application.properties");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        StreamsBuilder builder = new StreamsBuilder();
        //source processor giving us stream from topic input-words
        KStream<String, String> inputStream = builder.stream("csv", Consumed.with(Serdes.String(), Serdes.String()));
       KStream<String,String>flatted =inputStream.flatMapValues((value) ->{
                    String parts[]=value.split(",");
                    List<String> partsList=Arrays.asList(parts);
                    return partsList;
        } );

        flatted.peek(((key, value) -> System.out.println("key="+key+"value="+value)))
                .to("flatted",Produced.with(Serdes.String(),Serdes.String()));


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
