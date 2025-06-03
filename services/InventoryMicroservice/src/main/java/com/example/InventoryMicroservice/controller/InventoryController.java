package com.example.InventoryMicroservice.controller;

import com.example.InventoryMicroservice.service.InventoryService;
import com.example.InventoryMicroservice.serviceimpl.InventoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryServiceImpl inventoryService;

    // GET /inventory/{productId}
    @GetMapping("/{productId}")
    public int getStockLevel(@PathVariable Long productId) {
        return inventoryService.getStockLevel(productId);
    }

    // PUT /inventory/{productId}
    @PutMapping("/{productId}")
    public void updateStockLevel(@PathVariable Long productId, @RequestParam int updatedStockLevel) {
        inventoryService.updateStockLevel(productId, updatedStockLevel);
    }

    @PostMapping("/{productId}")
    public void addInventory(@PathVariable Long productId, @RequestParam int stockLevel) {
        inventoryService.addInventory(productId, stockLevel);
    }
}