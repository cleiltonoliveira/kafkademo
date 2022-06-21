package br.com.kafkademo.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Slf4j
@Component
public class TestListener {

    @KafkaListener(topics = "topic-1", groupId = "group-1")
    public void listen(String message, ConsumerRecordMetadata metadata) {
//        log.info("Thread: {}", Thread.currentThread().getId());
        log.info("Topic {} Pt {} Offset{}: {}", metadata.topic(), metadata.partition(), metadata.offset(), message);
        log.info("Timestamp {} ", LocalDateTime.ofInstant(Instant.ofEpochMilli(metadata.timestamp()), TimeZone.getDefault().toZoneId()));
    }
}
