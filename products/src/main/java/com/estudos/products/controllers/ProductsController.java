package com.estudos.products.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.estudos.products.dto.ProductDTO;
import com.estudos.products.services.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.extern.log4j.Log4j2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RestController
@RequestMapping("/product")
@Log4j2
public class ProductsController {

    @Autowired
    private ProductService productService;

    @GetMapping()
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable String stringID) {
        return ResponseEntity.ok(productService.getProductById(stringID));
    }

    @PostMapping
    public ResponseEntity<String> postProduct(@RequestParam("product") String productString,
            @RequestParam("file") MultipartFile file) throws JsonMappingException, JsonProcessingException {
        return ResponseEntity.ok(productService.postProduct(productString, file));
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String stringID) {
        productService.deleteProduct(stringID);
    }

}
