package com.estudos.products.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.estudos.products.dto.ProductDTO;
import com.estudos.products.entities.Category;
import com.estudos.products.entities.Product;
import com.estudos.products.infra.s3.dtos.UploadObjectDTO;
import com.estudos.products.infra.s3.service.S3Service;
import com.estudos.products.repositories.CategoryRepository;
import com.estudos.products.repositories.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.HashSet;
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
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ObjectMapper objectMapper;

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
    public ResponseEntity<String> postProduct(@RequestParam("product") String productString, @RequestParam("file") MultipartFile file) throws JsonMappingException, JsonProcessingException {
        ProductDTO dto = objectMapper.readValue(productString, ProductDTO.class);

        List<Category> categories = categoryRepository.findAllById(dto.categoriesIdList());

        Product product = Product.builder()
                .title(dto.title())
                .description(dto.description())
                .categories(new HashSet<Category>(categories))
                .price(dto.price())
                .build();

        Product persistenceProduct = productRepository.save(product);

        log.info(file.getName());

        UploadObjectDTO uploadObjectDTO = UploadObjectDTO.builder().id(persistenceProduct.getProductId()).name(persistenceProduct.getTitle()).file(file).build();
        
        try {
            s3Service.uploadFile(uploadObjectDTO);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }

}
