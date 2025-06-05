package com.example.OrderMicroservice.serviceimpl;

import com.example.OrderMicroservice.client.CartServiceClient;
import com.example.OrderMicroservice.model.Order;
import com.example.OrderMicroservice.model.OrderItem;
import com.example.OrderMicroservice.model.OrderItemId;
import com.example.OrderMicroservice.model.OrderRequest;
import com.example.OrderMicroservice.repository.OrderItemRepository;
import com.example.OrderMicroservice.repository.OrderRepository;
import com.example.OrderMicroservice.service.OrderService;
import com.example.OrderMicroservice.service.PaymentService;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CartServiceClient cartServiceClient;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PaymentService paymentService;

    @Override
    public Order placeOrder(OrderRequest orderRequest) {
        // Process payment
        boolean paymentSuccess = paymentService.processPayment(orderRequest.getPaymentDetails(), orderRequest.getTotalAmount());
        System.out.println(paymentSuccess);
        if (paymentSuccess) {
            Order order = new Order();
            order.setUserId(Long.parseLong(orderRequest.getUserId()));
            order.setAddress(orderRequest.getShippingDetails().getAddress());
            order.setCity(orderRequest.getShippingDetails().getCity());
            order.setState(orderRequest.getShippingDetails().getState());
            order.setZip(orderRequest.getShippingDetails().getZip());
            order.setTotalAmount(orderRequest.getTotalAmount());
            order.setStatus("Pending");
            order.setPaymentStatus("Paid");

// ✅ Create and set orderItems with reference to order
            List<OrderItem> orderItems = orderRequest.getOrderItems().stream()
                    .map(cartItem -> {
                        OrderItem orderItem = new OrderItem();
                        orderItem.setId(new OrderItemId(cartItem.getId().getProductId(), cartItem.getId().getTitle(), cartItem.getId().getCategory()));
                        orderItem.setDescription(cartItem.getDescription());
                        orderItem.setPrice(cartItem.getPrice());
                        orderItem.setImage(cartItem.getImage());
                        orderItem.setQuantity(cartItem.getQuantity());
                        orderItem.setOrder(order);  // ✅ Set Order reference here
                        return orderItem;
                    })
                    .collect(Collectors.toList());
            order.setItems(orderItems);  // ✅ Now, orderItems have the order reference
            Order savedOrder=orderRepository.save(order);
            return savedOrder;
        } else {
            throw new RuntimeException("Payment failed");
        }
    }

    @Override
    public List<Order> getOrder(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}