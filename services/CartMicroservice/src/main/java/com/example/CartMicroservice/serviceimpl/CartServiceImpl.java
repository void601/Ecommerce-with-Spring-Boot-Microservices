package com.example.CartMicroservice.serviceimpl;

import com.example.CartMicroservice.model.CartItem;
import com.example.CartMicroservice.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final HashOperations<String, String, List<CartItem>> hashOperations;
    private static final String CART_KEY = "CART";

    @Autowired
    public CartServiceImpl(RedisTemplate<String, List<CartItem>> redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void addItemsToCart(Long cartId, List<CartItem> items) {
        System.out.println("Adding to cart: " + cartId + ", items: " + items);
        hashOperations.put(CART_KEY, cartId.toString(), items);
    }

    @Override
    public void clearCart(Long cartId) {
        System.out.println("Clearing cart: " + cartId);
        hashOperations.delete(CART_KEY, cartId.toString());
    }

    @Override
    public List<CartItem> getCartItems(Long cartId) {
        System.out.println("Fetching cart items for: " + cartId);
        List<CartItem> items = hashOperations.get(CART_KEY, cartId.toString());
        System.out.println("Retrieved cart items: " + items);
        return items;
    }

    @Override
    public void addItemToCart(Long cartId, CartItem item) {
        List<CartItem> cartItem= Arrays.asList(item);
        System.out.println("Adding to cart: " + cartId + ", items: " + item);
        hashOperations.put(CART_KEY, cartId.toString(), cartItem);
    }
}