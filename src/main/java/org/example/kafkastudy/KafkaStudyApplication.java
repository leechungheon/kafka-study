package org.example.kafkastudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class KafkaStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaStudyApplication.class, args);
    }

}
