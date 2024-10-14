package com.estudos.products.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estudos.products.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

}
