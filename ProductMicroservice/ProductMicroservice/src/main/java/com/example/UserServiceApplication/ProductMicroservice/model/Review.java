package com.example.UserServiceApplication.ProductMicroservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class Review {
    private Integer rating;
    private String comment;
    private String date;
    private String reviewerName;
    private String reviewerEmail;
}
