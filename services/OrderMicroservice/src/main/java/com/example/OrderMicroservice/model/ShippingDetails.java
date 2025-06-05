package com.example.OrderMicroservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingDetails {
    private String name;
    private String address;
    private String city;
    private String state;
    private String zip;
}
