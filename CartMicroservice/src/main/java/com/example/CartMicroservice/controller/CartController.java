package com.example.CartMicroservice.controller;

import com.example.CartMicroservice.model.CartItem;
import com.example.CartMicroservice.serviceimpl.CartServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@AllArgsConstructor
class CartController {

    private final CartServiceImpl cartService;

    @PostMapping("/{cartId}/items")
    public ResponseEntity<String> addCartItems(@PathVariable Long cartId,@RequestBody List<CartItem> items) {
        cartService.addItemsToCart(cartId,items);
        return ResponseEntity.ok("Cart items added successfully!");
    }

    @PostMapping("/{cartId}/item")
    public ResponseEntity<String> addCartItem(@PathVariable Long cartId,@RequestBody CartItem cartItem) {
        // Process the single cart item
        cartService.addItemToCart(cartId,cartItem);
        return ResponseEntity.ok("Cart item added successfully!");
    }

    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<String> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.ok("Cart cleared successfully!");
    }

    @GetMapping("/{cartId}/items")
    public List<CartItem> getCartItems(@PathVariable Long cartId) {
        System.out.println("Cart: "+cartService.getCartItems(cartId));
        return cartService.getCartItems(cartId);
    }
}