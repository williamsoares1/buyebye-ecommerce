package com.estudos.products.infra.kafka.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record ExpiredDiscountKQDTO(String discountId, String productId, LocalDateTime endDate) {

}
