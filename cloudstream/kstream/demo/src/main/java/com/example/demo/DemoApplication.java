package com.example.demo;

import com.example.demo.deliveryms.DeliveryMessage;
import com.example.demo.deliveryms.DeliveryMessageSerde;
import com.example.demo.deliveryms.DeliveryStatus;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.kstream.internals.TimeWindow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.support.MessageBuilder;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public Serde<String> stringSerde() {
        return Serdes.String();
    }


    @Bean
    public Serde<DeliveryMessage> deliverySerde() {
        return new DeliveryMessageSerde();
    }

    @Bean
    public Function<KStream<String, String>, KStream<String, Integer>> wordProcess() {
        Function<KStream<String, String>, KStream<String, Integer>> function = (stream) -> {
            KStream<String, Integer> outStream = stream.mapValues(value -> value.length())
                    .peek((key, value) -> {
                        System.out.println("key=" + key + " value=" + value);
                    });
            return outStream;
        };

        return function;
    }


    @Bean
    public Function<KStream<String, String>, KStream<String, String>> flatDemo() {
        Function<KStream<String, String>, KStream<String, String>> function = (inputStream) -> {

            KStream<String, String> flatted = inputStream.flatMapValues((value) -> {
                String parts[] = value.split(",");
                List<String> partsList = Arrays.asList(parts);
                return partsList;
            });

            return flatted.peek(((key, value) -> System.out.println("key=" + key + "value=" + value)));


        };
        return function;

    }

    /*
     // in input topic, all delivery messages
        in output topic, only delivery messages which are delivered

     */

    @Bean
    public Function<KStream<String, DeliveryMessage>, KStream<String, DeliveryMessage>> filterDeliveries() {
        Function<KStream<String, DeliveryMessage>, KStream<String, DeliveryMessage>> function = (inputStream) -> {
            KStream<String, DeliveryMessage> filtered = inputStream
                    .peek((orderId, deliveryMsg) -> System.out.println("messages comming in key=" + orderId + "value=" + deliveryMsg))
                    .filter((orderId, deliverMsg) -> deliverMsg.getStatus().equals(DeliveryStatus.DELIVERED));
            return filtered.peek((orderId, deliveryMsg) -> {
                System.out.println("after filter key=" + orderId + "value=" + deliveryMsg);
            });
        };
        return function;
    }

    @Bean
    public Serde<Windowed<String>>windowedStringSerde(){
        return WindowedSerdes.timeWindowedSerdeFrom(String.class,1);
    }

    /**
     *  aggregrate on key on time window of 30 minute, adding serde for window separately
     *
     */
    @Bean
    public Function<KStream<String,String>,KStream<String,Long>>wordsCountByKeyInWindow(){
        KeyValueMapper<Windowed<String>,Long,KeyValue<String,Long>>mapper=(windowKey,value)->new KeyValue<>(windowKey.key(),value);
        Function<KStream<String,String>,KStream<String,Long>>function=inputStream->
                inputStream
                        .groupByKey()
                        .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(1)))
                        .count().toStream()
                        .map(mapper)
                        .peek((key,value)-> System.out.println("key="+key+" value="+value));
        return function;

    }



}