package com.example.OrderMicroservice.client;


import com.example.OrderMicroservice.model.CartItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CartServiceClient {

    @Value("${cart.service.url}") // URL of Cart Microservice
    private String cartServiceUrl;

    private final RestTemplate restTemplate;

    public CartServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<CartItem> getCartItems(Long cartId) {
        String url = cartServiceUrl + "/cart/" + cartId + "/items";
        ResponseEntity<List<CartItem>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CartItem>>() {}
        );
        return response.getBody();
    }

    public void clearCart(Long cartId) {
        String url = cartServiceUrl + "/cart/" + cartId + "/clear";
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE, // Specify DELETE method
                null, // No request body
                Void.class // No response body
        );
    }
}