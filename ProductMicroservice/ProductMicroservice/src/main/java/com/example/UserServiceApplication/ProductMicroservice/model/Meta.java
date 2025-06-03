package com.example.UserServiceApplication.ProductMicroservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class Meta {
    private String createdAt;
    private String updatedAt;
    private String barcode;
    private String qrCode;
}
