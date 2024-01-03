package com.example.demo;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;
import java.util.function.Function;

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
    public Function<KStream<String, String>, KStream<String, Integer>> wordProcess() {
        Function<KStream<String, String>, KStream<String, Integer>> function = (stream) -> {
          KStream<String,Integer>outStream=  stream.mapValues(value -> value.length())
                    .peek((key, value) -> {
                        System.out.println("key=" + key + " value=" + value);
                    });
          return outStream;
        };

        return function;
    }

}
