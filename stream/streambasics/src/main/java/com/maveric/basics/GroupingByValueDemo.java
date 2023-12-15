package com.maveric.basics;

import com.maveric.deliveryms.KafkaPropertiesReader;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

// Serde Serializer-Deserializer
public class GroupingByValueDemo {
    private static final Logger Log = LoggerFactory.getLogger(GroupingByValueDemo.class);

    private static boolean stop = false;

    public static void main(String[] args) throws Exception {

        Properties properties = KafkaPropertiesReader.read("application.properties");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        StreamsBuilder builder = new StreamsBuilder();
        //source processor giving us stream from topic csv
        KStream<String, String> inputStream = builder.stream("stream-input");
        KGroupedStream<String,String>groupedStream=inputStream.groupBy((key,value)->{
            //new key, in our case value is the new key
            return value;
        });
        KTable<String,Long>countTable=groupedStream.count();
        KStream<String,Long>outStream=countTable.toStream();
        outStream.peek((key,value)-> System.out.println("outstream peek, key="+key+" count="+value))
                .to("messages-value-count", Produced.with(Serdes.String(),Serdes.Long()));

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
