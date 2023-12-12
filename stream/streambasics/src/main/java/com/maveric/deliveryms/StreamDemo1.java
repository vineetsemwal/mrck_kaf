package com.maveric.deliveryms;

import org.apache.kafka.common.serialization.Serde;
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

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
// Serde Serializer-Deserializer
public class StreamDemo1 {
    private static final Logger Log= LoggerFactory.getLogger(StreamDemo1.class);
    public static void main(String[] args) throws Exception {

        Properties properties=KafkaPropertiesReader.read("application.properties");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass());

        Log.info("****properties"+properties);
        StreamsBuilder builder=new StreamsBuilder();
        KStream<String,String>upperStream= builder.stream("input-words", Consumed.with(Serdes.String(),Serdes.String()))
                .peek((key,value)-> Log.info("key="+key+"value="+value))
                .mapValues((value)->value.toUpperCase())
                .peek((key,value)-> System.out.println("key="+key+",value="+value));
Inte
        upperStream.mapValues(value -> value.length())
                .peek((key,value)-> System.out.println("key="+key+",value="+value))
                .to("words-length",Produced.with(Serdes.String(),Serdes.Integer()));

                upperStream.to("output-words", Produced.with(Serdes.String(), Serdes.String()));
       Topology topology= builder.build();
       Log.info("*****topology="+topology.describe());

        try(KafkaStreams streams=new KafkaStreams(topology,properties)) {
                streams.start();
                Thread.sleep(10000000);

        }


        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            Log.info("*** application closed");
        }));

    }

}
