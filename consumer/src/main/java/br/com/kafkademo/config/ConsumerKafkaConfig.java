package br.com.kafkademo.config;

import br.com.kafkademo.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.RecordInterceptor;
import org.springframework.kafka.support.converter.BatchMessagingMessageConverter;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;

@EnableKafka
@Configuration
@Slf4j
public class ConsumerKafkaConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        var configs = new HashMap<String, Object>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(configs);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(true);
        return factory;
    }

    @Bean
    public ConsumerFactory jsonConsumerFactory() {
        var configs = new HashMap<String, Object>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(configs);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory jsonKafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(jsonConsumerFactory());
        factory.setMessageConverter(new BatchMessagingMessageConverter(new JsonMessageConverter()));
        factory.setBatchListener(true);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, Person> personConsumerFactory() {
        var configs = new HashMap<String, Object>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        var jsonDeserializer = new JsonDeserializer<>(Person.class)
                .trustedPackages("*")
                .forKeys();
        return new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Person> personKafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Person>();
        factory.setConsumerFactory(personConsumerFactory());
//        factory.setRecordInterceptor(exampleInterceptor());
//        factory.setRecordInterceptor(adultInterceptor());
        factory.setCommonErrorHandler(defaultErrorHandler());
        return factory;
    }

    private DefaultErrorHandler defaultErrorHandler() {
        var recoverer = new DeadLetterPublishingRecoverer(new KafkaTemplate<>(pf()), (consumerRecord, e) -> new TopicPartition(consumerRecord.topic() + ".DLT", -1));
        return new DefaultErrorHandler(recoverer, new FixedBackOff(1000L, 2));
    }

    public ProducerFactory<String, Person> pf() {
        var configs = new HashMap<String, Object>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new JsonSerializer<>());
    }

    private RecordInterceptor<String, Person> exampleInterceptor() {
        return new RecordInterceptor<>() {
            @Override
            public ConsumerRecord<String, Person> intercept(ConsumerRecord<String, Person> record) {
                return record;
            }

            @Override
            public void success(ConsumerRecord<String, Person> record, Consumer<String, Person> consumer) {
                log.info("success");
            }

            @Override
            public void failure(ConsumerRecord<String, Person> record, Exception exception, Consumer<String, Person> consumer) {
                log.info("Failure");
            }
        };
    }

    private RecordInterceptor<String, Person> adultInterceptor() {
        return record -> {
            log.info("Record: {}", record);
            var person = record.value();
            return person.getAge() >= 18 ? record : null;
        };
    }
}
