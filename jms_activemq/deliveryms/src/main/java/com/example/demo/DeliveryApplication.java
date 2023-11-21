package com.example.demo;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;


@SpringBootApplication
public class DeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryApplication.class, args);
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsContainerFactory() {
		DefaultJmsListenerContainerFactory containerFactory = new DefaultJmsListenerContainerFactory();
		containerFactory.setPubSubDomain(true);
		containerFactory.setConnectionFactory(connectionFactory());
		//containerFactory.setMessageConverter(jacksonJmsMessageConverter());
		return containerFactory;
	}

	@Bean
	public CachingConnectionFactory connectionFactory() {
		CachingConnectionFactory cachConnectionFactory = new CachingConnectionFactory();
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		//connectionFactory.setBrokerURL("tcp://localhost:61616");
		cachConnectionFactory.setTargetConnectionFactory(connectionFactory);
		return cachConnectionFactory;
	}

}
