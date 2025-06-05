package com.example.OrderMicroservice.repository;

import com.example.OrderMicroservice.model.OrderItem;
import com.example.OrderMicroservice.model.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {
}
