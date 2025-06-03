package com.example.UserServiceApplication.ProductMicroservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="productapi")
public class ProductApiDb {
    @EmbeddedId
    private ProductApiDbId id;
    @Column(length = 1000)
    private String description;
    private Double price;
    @Column(length = 500)
    private String image;
}
