package com.estudos.discount.dto;

import java.math.BigDecimal;

import com.estudos.discount.entities.ENUM.DISCOUNTTYPE;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;

@Builder
public record DiscountDTO(String productId, BigDecimal discountValue, @Enumerated(EnumType.STRING) DISCOUNTTYPE discountType) {

}
