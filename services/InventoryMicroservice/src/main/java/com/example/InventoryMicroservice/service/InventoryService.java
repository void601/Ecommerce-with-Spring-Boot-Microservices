package com.example.InventoryMicroservice.service;

public interface InventoryService {
    int getStockLevel(Long id);
    void updateStockLevel(Long id,int updatedStockLevel);
    void addInventory(Long id, int stockLevel);
}
