package com.estudos.products.infra.kafka.config;

import java.util.HashMap;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.JsonMessageConverter;

@Configuration
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = true)
public class KafkaConfig {

    @Autowired
    private KafkaProperties properties;

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroup;

    @SuppressWarnings("rawtypes")
    private ConsumerFactory consumer() {
        var configs = new HashMap<String, Object>();

        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());

        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        configs.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);

        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(configs);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public ConcurrentKafkaListenerContainerFactory kafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumer());
        factory.setRecordMessageConverter(new JsonMessageConverter());

        return factory;
    }

}
