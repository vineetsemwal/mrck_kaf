package com.example.demo;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
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
	public Serde<String>stringSerde(){
		return Serdes.String();
	}

	@Bean
	public Function<String,String> wordProcess(){
		Function<String,String>function=word->{
			System.out.println("input word="+word);
			return word.toUpperCase();
		};
		return function;
	}

}
