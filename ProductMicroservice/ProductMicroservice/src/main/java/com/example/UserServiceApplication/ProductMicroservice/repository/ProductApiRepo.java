package com.example.UserServiceApplication.ProductMicroservice.repository;

import com.example.UserServiceApplication.ProductMicroservice.model.ProductApiDb;
import com.example.UserServiceApplication.ProductMicroservice.model.ProductApiDbId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductApiRepo extends JpaRepository<ProductApiDb, ProductApiDbId> {

    //ProductApiDb findByProductId(Long productId);
}
