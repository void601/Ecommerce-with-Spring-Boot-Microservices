package com.example.OrderMicroservice.model;

import jakarta.persistence.EmbeddedId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem implements Serializable {
    @EmbeddedId
    private OrderItemId id;
    private int quantity;
    private String description;
    private String image;
    private Double price;
}