package com.estudos.discount.infra.streams.config;

import java.util.Map;
import java.util.HashMap;

import java.time.LocalDateTime;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import com.estudos.discount.infra.kafka.dto.DiscountKDTO;
import com.estudos.discount.infra.streams.serdes.DiscountKDTOSerde;

import lombok.extern.log4j.Log4j2;

@Configuration
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = true)
@EnableKafkaStreams
@Log4j2
public class KafkaStreamsConfig {

    @Autowired
    private KafkaProperties properties;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfig() {
        Map<String, Object> props = new HashMap<>();

        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "discount-expiration-channel");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, DiscountKDTOSerde.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public KStream<String, DiscountKDTO> kStream(StreamsBuilder streamsBuilder) {
        KStream<String, DiscountKDTO> discountsStream = streamsBuilder.stream("product_discount",
                Consumed.with(Serdes.String(), new DiscountKDTOSerde()));

        discountsStream.filter((key, value) -> value.endDate().isBefore(LocalDateTime.now()))
                .to("product_discount_expiration");

        return discountsStream;
    }

}
