package org.example.kafkastudy;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    //KafkaTemplate은 Spring이 제공하는 Kafka 메시지 전송용 템플릿
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    //sendMessage 메서드를 통해 지정된 토픽에 메시지를 보냅니다.
    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
        System.out.println("메시지 전송: " + message + " (토픽: " + topic + ")");
    }
}