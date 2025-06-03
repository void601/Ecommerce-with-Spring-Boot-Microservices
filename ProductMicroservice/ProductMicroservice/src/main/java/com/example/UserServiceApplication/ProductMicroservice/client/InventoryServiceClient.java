package com.example.UserServiceApplication.ProductMicroservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class InventoryServiceClient {

    @Value("${inventory.service.url}") // URL of Cart Microservice
    private String inventoryServiceUrl;

    private final RestTemplate restTemplate;

    public InventoryServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void updateInventory(Long productId, int stockLevel) {
        // Construct the URL with the query parameter
        String url = inventoryServiceUrl + "/inventory/" + productId + "?stockLevel=" + stockLevel;

        // Send the POST request
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.POST, // Specify POST method
                null, // No request body
                Void.class // No response body
        );

        // Handle the response if needed
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Inventory updated successfully");
        } else {
            System.out.println("Failed to update inventory");
        }
    }
}