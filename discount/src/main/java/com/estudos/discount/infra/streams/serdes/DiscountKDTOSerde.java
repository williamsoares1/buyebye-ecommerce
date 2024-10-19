package com.estudos.discount.infra.streams.serdes;

import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.estudos.discount.infra.kafka.dto.DiscountKDTO;

public class DiscountKDTOSerde extends Serdes.WrapperSerde<DiscountKDTO> {
    public DiscountKDTOSerde() {
        super(new JsonSerializer<>(), new JsonDeserializer<>(DiscountKDTO.class));
    }
}
