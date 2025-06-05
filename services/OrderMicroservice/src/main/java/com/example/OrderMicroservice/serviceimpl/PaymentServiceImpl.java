package com.example.OrderMicroservice.serviceimpl;

import com.example.OrderMicroservice.model.PaymentDetails;
import com.example.OrderMicroservice.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    public PaymentServiceImpl() {
        Stripe.apiKey = "sk_test_123"; // Replace with your Stripe secret key
    }

    @Override
    public Boolean processPayment(PaymentDetails paymentDetails, Double amount) {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount); // Amount in cents
        params.put("currency", "USD");
        params.put("source", paymentDetails.getCardNumber());

        try {
           // Charge charge = Charge.create(params);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
