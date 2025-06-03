package com.example.UserServiceApplication.ProductMicroservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class Dimensions {
    private Double width;
    private Double height;
    private Double depth;
}
