package com.example.UserServiceApplication.ProductMicroservice.model;

import lombok.Data;

@Data
public class Rating {
    private Double rate;
    private Integer count;
}