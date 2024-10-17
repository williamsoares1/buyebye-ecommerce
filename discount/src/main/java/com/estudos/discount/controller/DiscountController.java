package com.estudos.discount.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estudos.discount.dto.DiscountDTO;
import com.estudos.discount.service.DiscountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/discount")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @PostMapping
    public ResponseEntity<String> applyDiscount(@RequestBody DiscountDTO dto) {
        return ResponseEntity.of(discountService.applyDiscount(dto));
    }

}
