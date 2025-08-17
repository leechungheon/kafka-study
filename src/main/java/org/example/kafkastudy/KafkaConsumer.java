package org.example.kafkastudy;

import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    // KafkaTemplate 주입
    public KafkaConsumer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
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
        System.out.println("[메인 컨슈머] 메시지 수신: " + message);

        // "error"라는 문자열이 포함된 메시지에서 예외 발생
        if (message.contains("error")) {
            System.err.println("[메인 컨슈머] 메시지 처리 실패! 예외를 던집니다.");
            throw new RuntimeException("메시지 처리 실패");
        }
    }

    //같은 클래스의 @RetryableTopic이 붙은 @KafkaListener와 한 쌍으로 묶여서 동작하는 메서드의 dlt를 재수행
    @DltHandler
    public void processDltMessage(String message, @Header(KafkaHeaders.ORIGINAL_TOPIC) String topic) {
        System.out.printf("[DLT 핸들러] DLT 메시지 수신: %s (원본 토픽: %s)%n", message, topic);

        // DLT 메시지 재처리 로직
        // 예시: 문제가 해결된 후, 원래 토픽으로 다시 보내서 재처리를 시도
        try {
            System.out.println("[DLT 핸들러] 메시지를 원래 토픽으로 다시 보냅니다: " + message);
            // KafkaTemplate을 사용해 메시지를 원래 토픽으로 다시 보냅니다.
        } catch (Exception e) {
            System.err.println("[DLT 핸들러] 메시지 재전송 실패: " + message);
            // 재전송마저 실패하면 로그를 남기거나 알림을 보냅니다.
        }
    }
}