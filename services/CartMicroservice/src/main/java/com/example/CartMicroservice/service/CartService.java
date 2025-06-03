package com.example.CartMicroservice.service;

import com.example.CartMicroservice.model.CartItem;

import java.util.List;

public interface CartService {

    List<CartItem> getCartItems(Long cartId);
    void addItemsToCart(Long cartId,List<CartItem> items);
    void addItemToCart(Long cartId,CartItem item);
    void clearCart(Long cartId);
}
