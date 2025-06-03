package com.example.UserServiceApplication.ProductMicroservice.controller;

import com.example.UserServiceApplication.ProductMicroservice.model.*;
import com.example.UserServiceApplication.ProductMicroservice.serviceimpl.LogServiceImpl;
import com.example.UserServiceApplication.ProductMicroservice.serviceimpl.ProductServiceImpl;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private LogServiceImpl logService;

    @GetMapping
    public Page<Product> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "5") int size) {
        logService.sendLog("ProductService", "INFO", "Fetching all products - Page: " + page + ", Size: " + size);
        return productService.getAllProducts(page, size);
    }

    @GetMapping("/list")
    public List<Product> getAllProducts() {
        logService.sendLog("ProductService", "INFO", "Fetching all products as a list.");
        return productService.getAllProductsList();
    }

    @RateLimiter(name = "myService", fallbackMethod = "rateLimitFallback")
    @GetMapping("/secure/api/list")
    @Transactional
    @RolesAllowed("USER")
    public List<ProductApiDb> getAllProductsApi() {
        logService.sendLog("ProductService", "INFO", "Fetching all API products.");
        try {
            fetchExternalProducts();
            fetchExternalDummyProducts();
            List<ProductApiDb> productApiDb = productService.getAllProductsApiList();
            logService.sendLog("ProductService", "INFO", "Successfully fetched API products.");
            return productApiDb;
        } catch (Exception e) {
            logService.sendLog("ProductService", "ERROR", "Error fetching API products: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        logService.sendLog("ProductService", "INFO", "Fetching product with ID: " + id);
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        logService.sendLog("ProductService", "INFO", "Fetching products by category: " + category);
        return productService.getProductsByCategory(category);
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String name) {
        logService.sendLog("ProductService", "INFO", "Searching for products with name: " + name);
        return productService.searchProducts(name);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        logService.sendLog("ProductService", "INFO", "Creating new product: " + product.getName());
        Product createdProduct = productService.createProduct(product);
        logService.sendLog("ProductService", "INFO", "Product created successfully with ID: " + createdProduct.getId());
        return ResponseEntity.ok(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        logService.sendLog("ProductService", "INFO", "Updating product with ID: " + id);
        Product updatedProduct = productService.updateProduct(id, product);
        logService.sendLog("ProductService", "INFO", "Product updated successfully.");
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        logService.sendLog("ProductService", "INFO", "Deleting product with ID: " + id);
        productService.deleteProduct(id);
        logService.sendLog("ProductService", "INFO", "Product deleted successfully.");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/external")
    public CompletableFuture<List<ProductDTOFake>> fetchExternalProducts() {
        logService.sendLog("ProductService", "INFO", "Fetching external Fake Store products.");
        return productService.getProductsFromFakeStore();
    }

    @GetMapping("dummy/external")
    public CompletableFuture<List<ProductDTO>> fetchExternalDummyProducts() {
        logService.sendLog("ProductService", "INFO", "Fetching external Dummy JSON products.");
        return productService.getProductsFromDummyJson();
    }

    @GetMapping("api/{uniqueKey}")
    public ResponseEntity<ProductApiDb> getProductByIdApi(@PathVariable String uniqueKey) {
        logService.sendLog("ProductService", "INFO", "Fetching API product with uniqueKey: " + uniqueKey);
        ProductApiDb productApiDb = productService.getProductByIdApi(uniqueKey);
        return ResponseEntity.ok(productApiDb);
    }

    public List<ProductApiDb> rateLimitFallback(Throwable t) {
        logService.sendLog("ProductService", "WARN", "Rate limit exceeded. Returning empty list.");
        return Collections.emptyList();
    }
}
