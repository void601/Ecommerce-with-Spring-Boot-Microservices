package com.example.OrderMicroservice.model;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId; // Auto-incrementing ID
    @Embedded
    private OrderItemId id;
    @Column(length = 1000)
    private String description;
    private Double price;
    @Column(length = 500)
    private String image;
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order; // Many-to-One relationship with Order
    // Getters and Setters
}