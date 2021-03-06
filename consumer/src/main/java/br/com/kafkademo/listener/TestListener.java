package br.com.kafkademo.listener;

import br.com.kafkademo.custom.PersonCustomListener;
import br.com.kafkademo.model.City;
import br.com.kafkademo.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TestListener {

    @KafkaListener(topics = "topic-1", groupId = "group-1")
    public void listen(List<String> messages) {
        log.info("Thread: {} Messages: {}", Thread.currentThread().getId(), messages);
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

    @PersonCustomListener(groupId = "group-1")
    public void create(Person person, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) long partitionId) {
//        log.info("Thread: {}", Thread.currentThread().getId());
        log.info("Create person: {} Partition {}", person, partitionId);
        throw new RuntimeException("Test Exception");
    }

    @PersonCustomListener(topics = "person-topic.DLT", groupId = "group-1")
    public void dlt(Person person, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) long partitionId) {
        log.info("DLT: {} Partition {}", person, partitionId);
    }

    @KafkaListener(topics = "city-topic", groupId = "group-1", containerFactory = "jsonKafkaListenerContainerFactory")
    public void create(List<Message<City>> messages) {
//        log.info("Thread: {}", Thread.currentThread().getId());
        log.info("Messages : {}", messages);
        var city = messages.get(0).getPayload();
        log.info("Cidade : {}", city);
        log.info("Headers : {}", messages.get(0).getHeaders());

    }
}
