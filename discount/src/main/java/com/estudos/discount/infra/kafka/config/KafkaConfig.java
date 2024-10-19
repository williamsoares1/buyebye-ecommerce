package com.estudos.discount.infra.kafka.config;

import java.time.Duration;
import java.util.HashMap;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.estudos.discount.infra.kafka.dto.DiscountKDTO;

@Configuration
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = true)
public class KafkaConfig {

    @Autowired
    private KafkaProperties properties;

    @SuppressWarnings("unchecked")
    @Bean
    public KafkaTemplate<String, DiscountKDTO> kafkaTemplate() {
        return new KafkaTemplate<>(producer());
    }

    @Bean
    public KafkaAdmin.NewTopics newTopics() {
        return new KafkaAdmin.NewTopics(
                TopicBuilder
                        .name("product_discount")
                        .partitions(1)
                        .config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(Duration.ofHours(2).toMillis()))
                        .build(),

                TopicBuilder
                        .name("product_discount_expiration")
                        .partitions(1)
                        .config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(Duration.ofHours(2).toMillis()))
                        .build());
    };

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public ProducerFactory producer() {
        var configs = new HashMap<String, Object>();

        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());

        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        configs.put(ProducerConfig.ACKS_CONFIG, "1");

        configs.put(ProducerConfig.RETRIES_CONFIG, 2);

        return new DefaultKafkaProducerFactory(configs);
    }

}
