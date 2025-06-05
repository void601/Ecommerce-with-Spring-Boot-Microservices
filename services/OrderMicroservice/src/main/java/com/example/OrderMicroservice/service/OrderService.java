package com.example.OrderMicroservice.service;

import com.example.OrderMicroservice.model.Order;
import com.example.OrderMicroservice.model.OrderRequest;

import java.util.List;

public interface OrderService {
    Order placeOrder(OrderRequest orderRequest);
    List<Order> getOrder(Long id);
}
