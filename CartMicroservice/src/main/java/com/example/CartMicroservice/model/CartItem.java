package com.example.CartMicroservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("CartItem")
public class CartItem implements Serializable {
    private Long productId;
    private int quantity;
    private String productName;
    private double price;
}