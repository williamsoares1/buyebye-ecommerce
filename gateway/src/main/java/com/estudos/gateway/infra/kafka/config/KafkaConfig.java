package com.estudos.gateway.infra.kafka.config;

import java.time.Duration;

import org.apache.kafka.common.config.TopicConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = true)
public class KafkaConfig {

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

}
