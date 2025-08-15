package org.example.kafkastudy;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfiguration {

    @Bean
    public NewTopic kafkaTestTopic() {
        return TopicBuilder.name("kafka-test")
                .partitions(3)
                .replicas(1)
                .build();
    }
}