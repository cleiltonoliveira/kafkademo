package br.com.kafkademo.listener;

import br.com.kafkademo.custom.PersonCustomListener;
import br.com.kafkademo.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestListener {

    @KafkaListener(topics = "topic-1", groupId = "group-1")
    public void listen(String message) {
        log.info("Thread: {} Message: {}", Thread.currentThread().getId(), message);
    }

    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void listen2(String message) {
        log.info("Thread: {} Message: {}", Thread.currentThread().getId(), message);
    }

//    @KafkaListener(groupId = "my-group", topicPartitions = {@TopicPartition(topic = "my-topic", partitions = "0")})
//    public void listen2(String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partitionId) {
//        log.info("Partition 0: {} Message: {}", partitionId, message);
//    }
//
//    @KafkaListener(groupId = "my-group", topicPartitions = {@TopicPartition(topic = "my-topic", partitions = "1-9")})
//    public void listen3(String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partitionId) {
//        log.info("Partition 1-9: {} Message: {}", partitionId, message);
//    }

//    @PersonCustomListener(groupId = "group-1")
//    public void create(Person person) {
//        log.info("Thread: {}", Thread.currentThread().getId());
//        log.info("Person : {}", person);
//    }
//
//    @PersonCustomListener(groupId = "group-2")
//    public void history(Person person) {
//        log.info("Thread: {}", Thread.currentThread().getId());
//        log.info("History : {}", person);
//    }
}
