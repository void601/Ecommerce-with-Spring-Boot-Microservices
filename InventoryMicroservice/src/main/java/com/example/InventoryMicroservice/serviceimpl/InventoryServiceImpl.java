package com.example.InventoryMicroservice.serviceimpl;

import com.example.InventoryMicroservice.model.Inventory;
import com.example.InventoryMicroservice.repository.InventoryRepository;
import com.example.InventoryMicroservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    // Get stock level for a product
    @Override
    public int getStockLevel(Long productId) {
        Inventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return inventory.getStockLevel();
    }

    // Update stock level for a product
    @Override
    public void updateStockLevel(Long productId, int newStockLevel) {
        Inventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        inventory.setStockLevel(newStockLevel);
        inventoryRepository.save(inventory);
    }
    //Add inventory
    @Override
    public void addInventory(Long productId, int stockLevel) {
        Inventory inventory=new Inventory();
        inventory.setStockLevel(stockLevel);
        inventoryRepository.save(inventory);
    }
}