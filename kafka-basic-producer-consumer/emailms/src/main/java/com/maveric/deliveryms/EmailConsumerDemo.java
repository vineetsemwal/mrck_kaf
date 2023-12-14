package com.maveric.deliveryms;

import kafka.server.KafkaConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class EmailConsumerDemo {
    private static final Logger Log= LoggerFactory.getLogger(EmailConsumerDemo.class);
    public static void main(String[] args) throws IOException,InterruptedException {
        Properties properties=KafkaPropertiesReader.read("application.properties");
        System.out.println("***properties="+properties);

        try(KafkaConsumer<String,DeliveryMessage>consumer=new KafkaConsumer<>(properties)){
            consumer.subscribe(List.of("delivered"));

            while (true) {
                ConsumerRecords<String, DeliveryMessage> records = consumer.poll(Duration.ofMillis(250));

                Log.info("*****records fetched="+records.count());
                for (ConsumerRecord<String, DeliveryMessage> iteratedRecord : records) {
                    Log.info("****iterating on records");
                    String key = iteratedRecord.key();
                    DeliveryMessage value = iteratedRecord.value();
                    Log.info("partition="+iteratedRecord.partition());
                    sendMail(value);


                }
                //consumer.commitAsync();
                Thread.sleep(5000);
            }


        }catch (Exception e){
            System.out.println("**************exception");
            e.printStackTrace();
        }
    }

    public static void sendMail(DeliveryMessage value){
        Log.info("sending mail for delivery message="+value.getOrderID()+"-"+value.getStatus());
    }
}
