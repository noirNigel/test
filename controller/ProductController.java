package org.example.demomanagementsystemcproject.controller;

import org.example.demomanagementsystemcproject.dto.ProductDTO;
import org.example.demomanagementsystemcproject.dto.ProductQueryDTO;
import org.example.demomanagementsystemcproject.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getProducts(ProductQueryDTO query) {
        return ResponseEntity.ok(productService.getProducts(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.createProduct(productDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, productDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/batch-delete")
    public ResponseEntity<Void> batchDelete(@RequestBody List<Long> ids) {
        productService.batchDelete(ids);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        productService.updateStatus(id, body.get("status"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/batch-status")
    public ResponseEntity<Void> batchUpdateStatus(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) body.get("ids");
        Integer status = (Integer) body.get("status");
        productService.batchUpdateStatus(ids, status);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Void> updateStock(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        productService.updateStock(id, body.get("stock"));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductDTO>> getLowStockProducts() {
        return ResponseEntity.ok(productService.getLowStockProducts());
    }

    @PutMapping("/{id}/warning-threshold")
    public ResponseEntity<Void> setWarningThreshold(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        productService.setWarningThreshold(id, body.get("threshold"));
        return ResponseEntity.ok().build();
    }
}