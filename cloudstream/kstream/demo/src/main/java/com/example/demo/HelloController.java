package com.example.demo;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private StreamBridge bridge;

    public HelloController(StreamBridge bridge) {
        this.bridge = bridge;
    }
    @PostMapping("/msg")
    public void sendMsg(@RequestBody String msgText) {
        //bridge.send("bridgeDemo-out-0", msgText);
        Message<String>message=MessageBuilder.withPayload(msgText)
                .setHeader(KafkaHeaders.KEY,msgText)
                        .build();
        bridge.send("bridgeDemo-out-0",message);
    }

}
