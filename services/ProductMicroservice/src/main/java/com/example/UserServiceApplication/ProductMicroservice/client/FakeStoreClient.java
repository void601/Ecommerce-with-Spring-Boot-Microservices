package com.example.UserServiceApplication.ProductMicroservice.client;

import com.example.UserServiceApplication.ProductMicroservice.model.FakeAPIProduct;
import com.example.UserServiceApplication.ProductMicroservice.serviceimpl.LogServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FakeStoreClient {

    private final RestTemplate restTemplate;
    private final LogServiceImpl logService; // Inject LogService

    public FakeStoreClient(RestTemplate restTemplate, LogServiceImpl logService) {
        this.restTemplate = restTemplate;
        this.logService = logService;
    }

    @Retry(name = "fakeStoreService", fallbackMethod = "getAllProductsFakeStoreRetryFallback")
    @CircuitBreaker(name = "fakeStoreService", fallbackMethod = "getAllProductsFakeStoreFallback")
    public List<FakeAPIProduct> getProductsFromFakeStore() {
        try {
            String url = "https://fakestoreapi.com/products";
            logService.sendLog("fake-store-client", "INFO", "Fetching products from Fake Store API");

            FakeAPIProduct[] productsArray = restTemplate.getForObject(url, FakeAPIProduct[].class);
            ObjectMapper objectMapper = new ObjectMapper();

            List<FakeAPIProduct> products = Arrays.stream(productsArray)
                    .map(product -> objectMapper.convertValue(product, FakeAPIProduct.class))
                    .collect(Collectors.toList());

            logService.sendLog("fake-store-client", "INFO", "Successfully fetched " + products.size() + " products from Fake Store API");
            return products;
        } catch (Exception e) {
            logService.sendLog("fake-store-client", "ERROR", "Exception occurred while fetching products from Fake Store API: " + e.getMessage());
            throw e; // Re-throw the exception to trigger retry/fallback
        }
    }

    public List<FakeAPIProduct> getAllProductsFakeStoreFallback(Exception t) {
        logService.sendLog("fake-store-client", "ERROR", "Fake Store API is down. Circuit Breaker fallback triggered due to: " + t.getMessage());
        return Collections.emptyList();
    }

    public List<FakeAPIProduct> getAllProductsFakeStoreRetryFallback(Exception t) {
        logService.sendLog("fake-store-client", "ERROR", "Fake Store API is down (From Retry fallback). Exception: " + t.getMessage());
        return Collections.emptyList();
    }
}