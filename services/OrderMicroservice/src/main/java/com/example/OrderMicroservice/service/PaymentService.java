package com.example.OrderMicroservice.service;

import com.example.OrderMicroservice.model.PaymentDetails;

public interface PaymentService {

    Boolean processPayment(PaymentDetails paymentDetails, Double amount);
}
