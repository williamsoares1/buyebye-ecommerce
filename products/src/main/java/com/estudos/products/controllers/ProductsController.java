package com.estudos.products.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.estudos.products.dto.ProductDTO;
import com.estudos.products.entities.Category;
import com.estudos.products.entities.Product;
import com.estudos.products.repositories.CategoryRepository;
import com.estudos.products.repositories.ProductRepository;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RestController
@RequestMapping("/product")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping()
    public ResponseEntity<List<ProductDTO>> getAllProducts() {

        List<ProductDTO> list = productRepository.findAll()
                .stream()
                .map(p -> ProductDTO.builder()
                        .title(p.getTitle())
                        .description(p.getDescription())
                        .price(p.getPrice())
                        .build())
                .toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getMethodName(@PathVariable Long id) {

        ProductDTO product = productRepository.findById(id)
                .map(p -> ProductDTO.builder()
                        .title(p.getTitle())
                        .description(p.getDescription())
                        .price(p.getPrice())
                        .build())
                .get();

        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<String> postProduct(@RequestBody ProductDTO dto) {

        List<Category> categories = categoryRepository.findAllById(dto.categoriesIdList());

        Product product = Product.builder()
                .title(dto.title())
                .description(dto.description())
                .categories(new HashSet<Category>(categories))
                .price(dto.price())
                .build();

        productRepository.save(product);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }

}
