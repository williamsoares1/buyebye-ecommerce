package com.estudos.discount.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record DiscountDTO(String productId, BigDecimal discountValue, String discountType) {

}
