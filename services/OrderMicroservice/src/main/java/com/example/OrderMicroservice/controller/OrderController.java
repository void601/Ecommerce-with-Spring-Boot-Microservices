package com.example.OrderMicroservice.controller;

import com.example.OrderMicroservice.model.Order;
import com.example.OrderMicroservice.model.OrderRequest;
import com.example.OrderMicroservice.serviceimpl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest orderRequest) {
        Order order = orderService.placeOrder(orderRequest);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Order>> getOrder(@PathVariable Long id) {
        System.out.println(id);
        List<Order> order = orderService.getOrder(id);
        return ResponseEntity.ok(order);
    }
}