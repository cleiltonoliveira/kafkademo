package br.com.kafkademo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.IntStream;

@RestController
public class TestController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

//    @Autowired
//    private KafkaTemplate<String, Serializable> jsonKafkaTemplate;

    @GetMapping("send")
    public ResponseEntity<?> send() {
        IntStream.range(1, 10).boxed().forEach(n ->
                kafkaTemplate.send("topic-1", " Message: " + n));

        return ResponseEntity.ok().build();
    }

//    @GetMapping("send-person")
//    public void sendPerson() {
//        jsonKafkaTemplate.send("person-topic", new Person("João", new Random().nextInt(50)));
//    }

}