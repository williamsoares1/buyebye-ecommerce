package com.estudos.products.infra.kafka.dto;

import java.math.BigDecimal;

import com.estudos.products.infra.kafka.ENUM.DISCOUNTTYPE;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;

@Builder
public record DiscountKDTO(String discountId, String productId, BigDecimal discountValue, @Enumerated(EnumType.STRING) DISCOUNTTYPE discountType) {

}
