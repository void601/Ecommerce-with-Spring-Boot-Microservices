package com.example.OrderMicroservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   // private Long userId; can be fetched from token
    private Long userId;
    private String address;
    private String city;
    private String state;
    private String zip;
    private Double totalAmount;
    private String status; // Pending, Confirmed, Shipped, Cancelled
    private String paymentStatus; // Paid, Failed

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items; // One-to-Many relationship with OrderItem

    // Getters and Setters
}