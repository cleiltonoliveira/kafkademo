package br.com.kafkademo.controller;

import br.com.kafkademo.model.City;
import br.com.kafkademo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class TestController {

    @Autowired
    private RoutingKafkaTemplate kafkaTemplate;

    @GetMapping("send")
    public void send() {
        kafkaTemplate.send("topic-1", "Teste do topíc-1");
    }

    @GetMapping("send-person")
    public void sendPerson() {
        kafkaTemplate.send("person-topic", new Person("João", new Random().nextInt(50)));
    }

    @GetMapping("send-city")
    public void sendCity() {
        kafkaTemplate.send("city-topic", new City("Iraquara", "BA"));
    }
}