package com.estudos.discount.service;

import java.util.Optional;
import java.util.UUID;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estudos.discount.dto.DiscountDTO;
import com.estudos.discount.entities.Discount;
import com.estudos.discount.infra.kafka.dto.DiscountKDTO;
import com.estudos.discount.infra.kafka.sender.KafkaDiscountSender;
import com.estudos.discount.repositories.DiscountRepository;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private KafkaDiscountSender kafkaDiscountSender;

    public Optional<String> applyDiscount(DiscountDTO dto) {

        Discount discount = Discount.builder()
                .productId(UUID.fromString(dto.productId()))
                .discountType(dto.discountType())
                .discountValue(dto.discountValue())
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2))
                .build();

        discountRepository.save(discount);

        DiscountKDTO discountKDTO = DiscountKDTO.builder()
                .discountType(dto.discountType())
                .discountValue(dto.discountValue())
                .productId(dto.productId())
                .build();

        kafkaDiscountSender.applyDiscount(discountKDTO);

        return Optional.of("foi");

    }
}
