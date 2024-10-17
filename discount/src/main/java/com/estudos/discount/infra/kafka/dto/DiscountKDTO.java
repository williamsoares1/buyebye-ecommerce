package com.estudos.discount.infra.kafka.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record DiscountKDTO(String productId, BigDecimal discountValue, String discountType) {

}
