package com.estudos.discount.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.estudos.discount.entities.ENUM.DISCOUNTTYPE;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Discount {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID discountId;
    private UUID productId;
    private BigDecimal discountValue;
    private DISCOUNTTYPE discountType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean notified;
}
