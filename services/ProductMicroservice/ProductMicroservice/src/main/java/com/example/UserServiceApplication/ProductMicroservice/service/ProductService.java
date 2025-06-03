package com.example.UserServiceApplication.ProductMicroservice.service;


import com.example.UserServiceApplication.ProductMicroservice.model.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ProductService {

    Page<Product> getAllProducts(int page, int size);

    List<Product> getAllProductsList();

    List<ProductApiDb> getAllProductsApiList();

    Product getProductById(Long id);

    List<Product> getProductsByCategory(String category);

    List<Product> searchProducts(String name);

    Product createProduct(Product product);

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long id);

    CompletableFuture<List<ProductDTOFake>> getProductsFromFakeStore();

    CompletableFuture<List<ProductDTO>> getProductsFromDummyJson();

    ProductApiDb getProductByIdApi(String uniqueKey);

    //ProductApiDb getProductByIdJsonApi(Long uniqueKey);
}
