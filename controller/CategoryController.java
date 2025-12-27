package org.example.demomanagementsystemcproject.controller;

import org.example.demomanagementsystemcproject.dto.CategoryDTO;
import org.example.demomanagementsystemcproject.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/tree")
    public ResponseEntity<List<CategoryDTO>> getCategoryTree() {
        return ResponseEntity.ok(categoryService.getCategoryTree());
    }

    @GetMapping("/children/{parentId}")
    public ResponseEntity<List<CategoryDTO>> getChildren(@PathVariable Long parentId) {
        return ResponseEntity.ok(categoryService.getChildren(parentId));
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.createCategory(categoryDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/sort")
    public ResponseEntity<Void> updateSortOrder(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        categoryService.updateSortOrder(id, body.get("sortOrder"));
        return ResponseEntity.ok().build();
    }
}