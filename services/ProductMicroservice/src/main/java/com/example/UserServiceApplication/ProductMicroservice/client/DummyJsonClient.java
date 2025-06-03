package com.example.UserServiceApplication.ProductMicroservice.client;

import com.example.UserServiceApplication.ProductMicroservice.model.DummyProduct;
import com.example.UserServiceApplication.ProductMicroservice.serviceimpl.LogServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DummyJsonClient {

    private final RestTemplate restTemplate;
    private final LogServiceImpl logService; // Inject LogService

    public DummyJsonClient(RestTemplate restTemplate, LogServiceImpl logService) {
        this.restTemplate = restTemplate;
        this.logService = logService;
    }

    @Retry(name = "dummyJsonService", fallbackMethod = "getAllProductsDummyRetryFallback")
    @CircuitBreaker(name = "dummyJsonService", fallbackMethod = "getAllProductsDummyFallback")
    public List<DummyProduct> getProductsFromDummyJson() {
        try {
            String url = "https://dummyjson.com/products";
            logService.sendLog("dummy-json-client", "INFO", "Fetching products from Dummy JSON API");
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null || !response.containsKey("products")) {
                logService.sendLog("dummy-json-client", "ERROR", "Invalid response from Dummy JSON API");
                throw new RuntimeException("Invalid response from Dummy JSON API");
            }

            List<Map<String, Object>> map = (List<Map<String, Object>>) response.get("products");
            ObjectMapper objectMapper = new ObjectMapper();

            List<DummyProduct> products = map.stream()
                    .map(mapObject -> objectMapper.convertValue(mapObject, DummyProduct.class))
                    .collect(Collectors.toList());

            logService.sendLog("dummy-json-client", "INFO", "Successfully fetched " + products.size() + " products from Dummy JSON API");
            return products;
        } catch (Exception e) {
            logService.sendLog("dummy-json-client", "ERROR", "Exception occurred while fetching products from Dummy JSON API: " + e.getMessage());
            throw e; // Ensure exception is thrown for retry to catch
        }
    }

    public List<DummyProduct> getAllProductsDummyFallback(Exception t) {
        logService.sendLog("dummy-json-client", "ERROR", "Dummy JSON API is down. Circuit Breaker fallback triggered due to: " + t.getMessage());
        return Collections.emptyList();
    }

    public List<DummyProduct> getAllProductsDummyRetryFallback(Exception t) {
        logService.sendLog("dummy-json-client", "ERROR", "Dummy JSON API is down (From Retry fallback). Exception: " + t.getMessage());
        return Collections.emptyList();
    }
}