package com.estudos.products.infra.kafka.listener;

import org.springframework.stereotype.Service;

import com.estudos.products.entities.Product;
import com.estudos.products.infra.kafka.dto.DiscountKDTO;
import com.estudos.products.repositories.ProductRepository;

import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

@Service
@Log4j2
public class ProductKafkaListener {

    @Autowired
    private ProductRepository productRepository;

    @KafkaListener(topics = "product_discount", groupId = "api-buyebye-products")
    public void listen(DiscountKDTO dto) {
        Optional<Product> productOpt = productRepository.findById(UUID.fromString(dto.productId()));

        if (productOpt.isPresent()) {
            Product product = productOpt.get();

            product.setDiscountApplied(true);

            if (dto.discountType().equals("percent"))
                product.setCurrentPrice(product.getOriginalPrice().subtract(
                        product.getOriginalPrice().multiply(dto.discountValue().divide(new BigDecimal(100)))));

            if (dto.discountType().equals("absolute"))
                product.setCurrentPrice(product.getOriginalPrice().subtract(dto.discountValue()));

            productRepository.save(product);
            log.info("::::::::::::::::::::::::::::::::::::: Chegou, confira");
        }

    }
}
