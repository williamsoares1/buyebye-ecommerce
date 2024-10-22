package com.estudos.discount.infra.kafka.dto;

import java.math.BigDecimal;

import com.estudos.discount.entities.ENUM.DISCOUNTTYPE;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;

@Builder
public record DiscountKDTO(String discountId, String productId, BigDecimal discountValue, @Enumerated(EnumType.STRING) DISCOUNTTYPE discountType) {

}
