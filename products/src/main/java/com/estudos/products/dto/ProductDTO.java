package com.estudos.products.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;

@Builder
public record ProductDTO(List<Long> categoriesIdList, String title, String description, BigDecimal currentPrice, BigDecimal originalPrice, boolean discountApplied) {

}
