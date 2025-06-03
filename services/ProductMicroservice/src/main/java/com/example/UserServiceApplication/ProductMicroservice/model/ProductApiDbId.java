package com.example.UserServiceApplication.ProductMicroservice.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductApiDbId implements Serializable {
    private Long productId;
    private String title;
    private String category;

}