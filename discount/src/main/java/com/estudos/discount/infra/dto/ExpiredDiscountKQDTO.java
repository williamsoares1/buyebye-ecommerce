package com.estudos.discount.infra.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record ExpiredDiscountKQDTO(String discountId, String productId, LocalDateTime endDate) {

}
