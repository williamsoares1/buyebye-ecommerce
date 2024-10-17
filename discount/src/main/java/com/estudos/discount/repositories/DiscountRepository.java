package com.estudos.discount.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estudos.discount.entities.Discount;

public interface DiscountRepository extends JpaRepository<Discount, UUID> {

}
