package org.example.kafkastudy;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    // 100ms, 200ms, 400ms 간격으로 총 3번 재시도
    @RetryableTopic(
            attempts = "4", // 초기 시도 1회 + 재시도 3회
            backoff = @Backoff(delay = 100),
            retryTopicSuffix  = "-retry", // 재시도 토픽 이름에 붙는 접미사
            dltTopicSuffix = "-dlt" // 데드레터 토픽 이름에 붙는 접미사
    )

    //@KafkaListener 어노테이션을 사용하면 특정 토픽(topics = "my-topic")에서 메시지를 수신
    //지정된 그룹(groupId = "my-group")에 속한 컨슈머로 동작. 메시지를 받으면 consume 메서드가 자동으로 호출
    @KafkaListener(topics = "kafka-test", groupId = "my-group")
    public void consume(String message) {
        System.out.println("메시지 수신: " + message);

        // "error"라는 문자열이 포함된 메시지에서 예외 발생
        if (message.contains("error")) {
            System.err.println("메시지 처리 실패! 예외를 던집니다.");
            throw new RuntimeException("메시지 처리 실패");
        }
    }
}