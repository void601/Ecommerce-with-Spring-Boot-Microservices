package com.example.UserServiceApplication.ProductMicroservice.serviceimpl;

import com.example.UserServiceApplication.ProductMicroservice.client.DummyJsonClient;
import com.example.UserServiceApplication.ProductMicroservice.client.FakeStoreClient;
import com.example.UserServiceApplication.ProductMicroservice.client.InventoryServiceClient;
import com.example.UserServiceApplication.ProductMicroservice.exception.ProductNotFoundException;
import com.example.UserServiceApplication.ProductMicroservice.model.*;
import com.example.UserServiceApplication.ProductMicroservice.repository.ProductApiRepo;
import com.example.UserServiceApplication.ProductMicroservice.repository.ProductRepository;
import com.example.UserServiceApplication.ProductMicroservice.service.ProductService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductApiRepo productApiRepo;

    @Autowired
    private InventoryServiceClient inventoryServiceClient;

    @Autowired
    private FakeStoreClient fakeStoreClient;

    @Autowired
    private DummyJsonClient dummyJsonClient;

    @Autowired
    private LogServiceImpl logService; // Inject LogService

    @Override
    public Page<Product> getAllProducts(int page, int size) {
        logService.sendLog("product-service", "INFO", "Fetching all products with pagination (page: " + page + ", size: " + size + ")");
        return productRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public List<Product> getAllProductsList() {
        logService.sendLog("product-service", "INFO", "Fetching all products as a list");
        return productRepository.findAll();
    }

    @Override
    public List<ProductApiDb> getAllProductsApiList() {
        logService.sendLog("product-service", "INFO", "Fetching all products from API database");
        return productApiRepo.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        logService.sendLog("product-service", "INFO", "Fetching product by ID: " + id);
        return productRepository.findById(id).orElseThrow(() -> {
            logService.sendLog("product-service", "ERROR", "Product not found with ID: " + id);
            return new ProductNotFoundException(id);
        });
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        logService.sendLog("product-service", "INFO", "Fetching products by category: " + category);
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> searchProducts(String name) {
        logService.sendLog("product-service", "INFO", "Searching products by name: " + name);
        return productRepository.findByNameContaining(name);
    }

    @Override
    public Product createProduct(Product product) {
        logService.sendLog("product-service", "INFO", "Creating product with name: " + product.getName());
        inventoryServiceClient.updateInventory(product.getId(), product.getStock());
        Product savedProduct = productRepository.save(product);
        logService.sendLog("product-service", "INFO", "Product created successfully with ID: " + savedProduct.getId());
        return savedProduct;
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        logService.sendLog("product-service", "INFO", "Updating product with ID: " + id);
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> {
            logService.sendLog("product-service", "ERROR", "Product not found with ID: " + id);
            return new ProductNotFoundException(id);
        });
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setStock(product.getStock());
        inventoryServiceClient.updateInventory(product.getId(), product.getStock());
        Product updatedProduct = productRepository.save(existingProduct);
        logService.sendLog("product-service", "INFO", "Product updated successfully with ID: " + updatedProduct.getId());
        return updatedProduct;
    }

    @Override
    public void deleteProduct(Long id) {
        logService.sendLog("product-service", "INFO", "Deleting product with ID: " + id);
        productRepository.deleteById(id);
        logService.sendLog("product-service", "INFO", "Product deleted successfully with ID: " + id);
    }

    @Transactional
    public void mapToProductApiDb(List<ProductDTO> productApiDtos) {
        logService.sendLog("product-service", "INFO", "Mapping ProductDTO list to ProductApiDb");
        List<ProductApiDb> list = productApiDtos.stream()
                .map(dto -> new ProductApiDb(
                        new ProductApiDbId(dto.getId(), dto.getTitle(), dto.getCategory()), // Composite key
                        dto.getDescription(),
                        dto.getPrice(),
                        dto.getImage()
                ))
                .collect(Collectors.toList());
        productApiRepo.saveAll(list);
        logService.sendLog("product-service", "INFO", "ProductDTO list mapped and saved to ProductApiDb");
    }

    @Transactional
    public void mapToProductApiDb2(List<ProductDTOFake> productApiDtos) {
        logService.sendLog("product-service", "INFO", "Mapping ProductDTOFake list to ProductApiDb");
        List<ProductApiDb> list = productApiDtos.stream()
                .map(dto -> new ProductApiDb(
                        new ProductApiDbId(dto.getId(), dto.getTitle(), dto.getCategory()), // Composite key
                        dto.getDescription(),
                        dto.getPrice(),
                        dto.getImage()
                ))
                .collect(Collectors.toList());
        productApiRepo.saveAll(list);
        logService.sendLog("product-service", "INFO", "ProductDTOFake list mapped and saved to ProductApiDb");
    }

    @Bulkhead(name = "externalService", type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "fallBackBulkhead")
    @TimeLimiter(name = "exampleTimeout", fallbackMethod = "timeoutFallback")
    @CircuitBreaker(name = "fakeStoreService", fallbackMethod = "getAllProductsFakeStoreFallback")
    @Override
    @Async
    public CompletableFuture<List<ProductDTOFake>> getProductsFromFakeStore() {
        logService.sendLog("product-service", "INFO", "Fetching products from Fake Store API");
        return CompletableFuture.supplyAsync(() -> {
            List<FakeAPIProduct> fakeAPIProducts = fakeStoreClient.getProductsFromFakeStore();
            List<ProductDTOFake> productDTOs = fakeAPIProducts.stream()
                    .map(ProductDTOFake::new)
                    .toList();
            mapToProductApiDb2(productDTOs);
            logService.sendLog("product-service", "INFO", "Products fetched and mapped from Fake Store API");
            return productDTOs;
        });
    }

    @Bulkhead(name = "myServiceBulkhead", type = Bulkhead.Type.THREADPOOL, fallbackMethod = "fallbackThreadPool")
    @TimeLimiter(name = "exampleTimeout", fallbackMethod = "timeoutFallback")
    @CircuitBreaker(name = "dummyJsonService", fallbackMethod = "getAllProductsDummyFallback")
    @Override
    @Async
    public CompletableFuture<List<ProductDTO>> getProductsFromDummyJson() {
        logService.sendLog("product-service", "INFO", "Fetching products from Dummy JSON API");
        return CompletableFuture.supplyAsync(() -> {
            List<DummyProduct> products = dummyJsonClient.getProductsFromDummyJson(); // Fetch products from service
            List<ProductDTO> productDTOs = products.stream()
                    .map(ProductDTO::new)
                    .toList();
            mapToProductApiDb(productDTOs);
            logService.sendLog("product-service", "INFO", "Products fetched and mapped from Dummy JSON API");
            return productDTOs;
        });
    }

    @Override
    public ProductApiDb getProductByIdApi(String uniqueKey) {
        logService.sendLog("product-service", "INFO", "Fetching product by unique key: " + uniqueKey);
        // Split the uniqueKey into parts
        String[] parts = uniqueKey.split("--", 3); // Split into 3 parts: id, title, category
        if (parts.length != 3) {
            logService.sendLog("product-service", "ERROR", "Invalid uniqueKey format: " + uniqueKey);
            throw new IllegalArgumentException("Invalid uniqueKey format");
        }
        // Decode URL-encoded parts (e.g., %20 -> space)
        Long id = Long.parseLong(parts[0]);
        String title = java.net.URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
        String category = java.net.URLDecoder.decode(parts[2], StandardCharsets.UTF_8);
        logService.sendLog("product-service", "INFO", "Decoded unique key - ID: " + id + ", Title: " + title + ", Category: " + category);
        ProductApiDbId productApiDbId = new ProductApiDbId(id, title, category);
        return productApiRepo.findById(productApiDbId).orElseThrow(() -> {
            logService.sendLog("product-service", "ERROR", "Product not found with unique key: " + uniqueKey);
            return new ProductNotFoundException(id);
        });
    }

    public CompletableFuture<List<DummyProduct>> getAllProductsDummyFallback(Throwable t) {
        logService.sendLog("product-service", "ERROR", "Dummy JSON API is down. Fallback triggered due to: " + t.getMessage());
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

    public CompletableFuture<List<FakeAPIProduct>> getAllProductsFakeStoreFallback(Throwable t) {
        logService.sendLog("product-service", "ERROR", "Fake Store API is down. Fallback triggered due to: " + t.getMessage());
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

    public CompletableFuture<List<FakeAPIProduct>> timeoutFallback(Throwable t) {
        logService.sendLog("product-service", "ERROR", "Request timed out. Fallback triggered due to: " + t.getMessage());
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

    public CompletableFuture<List<ProductDTO>> fallbackThreadPool(Throwable t) {
        logService.sendLog("product-service", "ERROR", "Thread pool is full. Fallback triggered due to: " + t.getMessage());
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

    public CompletableFuture<List<ProductDTO>> fallBackBulkhead(Throwable t) {
        logService.sendLog("product-service", "ERROR", "Concurrent request limit exceeded. Fallback triggered due to: " + t.getMessage());
        return CompletableFuture.completedFuture(Collections.emptyList());
    }
}