package com.estudos.discount.infra.kafka.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.estudos.discount.infra.kafka.dto.DiscountKDTO;

@Component
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = true)
public class KafkaDiscountSender {

    @Autowired
    private KafkaTemplate<String, DiscountKDTO> kafkaTemplate;

    public void applyDiscount(DiscountKDTO dto) {
        kafkaTemplate.send("product_discount", dto.discountId(), dto);
    }

    public void notifyExpiratedDiscount(DiscountKDTO dto){
        kafkaTemplate.send("product_discount_expiration", dto.discountId(), dto);
    }

}
