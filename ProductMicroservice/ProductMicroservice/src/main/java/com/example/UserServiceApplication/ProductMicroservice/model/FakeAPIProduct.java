package com.example.UserServiceApplication.ProductMicroservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FakeAPIProduct {
    private Long id;
    private String title;
    private String description;
    private String category;
    private Double price;
    private String image;
    private Rating ratings;
}
