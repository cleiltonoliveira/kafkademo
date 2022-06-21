package br.com.kafkademo.listener;

import br.com.kafkademo.custom.PersonCustomListener;
import br.com.kafkademo.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestListener {

    @KafkaListener(topics = "topic-1", groupId = "group-1")
    public void listen(String message) {
        log.info(message);
    }

    //    @KafkaListener(topics = "person-topic", groupId = "group-1", containerFactory = "personKafkaListenerContainerFactory")
    @PersonCustomListener(groupId = "group-1")
    public void create(Person person) {
        log.info("Thread: {}", Thread.currentThread().getId());
        log.info("Person : {}", person);
    }

    @PersonCustomListener(groupId = "group-2")
    public void history(Person person) {
        log.info("Thread: {}", Thread.currentThread().getId());
        log.info("History : {}", person);
    }
}
