package com.example.UserServiceApplication.ProductMicroservice.repository;


import com.example.UserServiceApplication.ProductMicroservice.model.Product;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @QueryHints(@QueryHint(name="javax.persistence.query.timeout",value = "5000"))
    Page<Product> findAll(Pageable pageable);
    @QueryHints(@QueryHint(name="javax.persistence.query.timeout",value = "5000"))
    List<Product> findByCategory(String category); // Find products by category
    @QueryHints(@QueryHint(name="javax.persistence.query.timeout",value = "5000"))
    List<Product> findByNameContaining(String name); // Search products by name
}