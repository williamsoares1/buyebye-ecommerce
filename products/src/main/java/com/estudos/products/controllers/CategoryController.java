package com.estudos.products.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estudos.products.dto.CategoryDTO;
import com.estudos.products.entities.Category;
import com.estudos.products.repositories.CategoryRepository;

@Controller
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {

        List<CategoryDTO> categories = categoryRepository.findAll()
                .stream()
                .map(c -> CategoryDTO.builder()
                        .title(c.getTitle())
                        .build())
                .toList();

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getMethodName(@PathVariable Long id) {

        CategoryDTO category = categoryRepository.findById(id)
                .map(c -> CategoryDTO.builder()
                        .title(c.getTitle())
                        .build())
                .get();

        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<String> postCategory(@RequestBody CategoryDTO dto) {

        Category category = Category.builder()
                .title(dto.title())
                .build();

        categoryRepository.save(category);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id){
        categoryRepository.deleteById(id);
    }
}
