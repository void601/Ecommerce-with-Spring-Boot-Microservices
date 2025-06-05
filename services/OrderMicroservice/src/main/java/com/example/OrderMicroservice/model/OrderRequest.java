package com.example.OrderMicroservice.model;

import com.example.OrderMicroservice.model.PaymentDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private String userId;
    private Double totalAmount;
    private List<OrderItem> orderItems;
    private PaymentDetails paymentDetails;
    private ShippingDetails shippingDetails;
    // Getters and Setters
}