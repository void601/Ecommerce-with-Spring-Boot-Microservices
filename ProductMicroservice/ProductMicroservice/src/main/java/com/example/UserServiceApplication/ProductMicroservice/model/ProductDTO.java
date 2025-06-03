package com.example.UserServiceApplication.ProductMicroservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {
    private Long id;
    private String title;
    private String description;
    private String category;
    private Double price;
    private String image;
//    private Double discountPercentage;
//    private Double rating;
//    private Integer stock;
//    private List<String> tags;
//    private String brand;
//    private String sku;
//    private Integer weight;
//    private Dimensions dimensions;
//    private String warrantyInformation;
//    private String shippingInformation;
//    private String availabilityStatus;
//    private List<Review> reviews;
//    private String returnPolicy;
//    private Integer minimumOrderQuantity;
//    private Meta meta;
//    private String image;  // ✅ Single image instead of List
//    private String thumbnail;

    // ✅ Constructor to map DummyProduct to com.example.UserServiceApplication.ProductMicroservice.model.DummyProductDTO
    public ProductDTO(DummyProduct product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.description = product.getDescription();
        this.category = product.getCategory();
        this.price = product.getPrice();
//        this.discountPercentage = product.getDiscountPercentage();
//        this.rating = product.getRating();
//        this.stock = product.getStock();
//        this.tags = product.getTags();
//        this.brand = product.getBrand();
//        this.sku = product.getSku();
//        this.weight = product.getWeight();
//        this.dimensions = product.getDimensions();
//        this.warrantyInformation = product.getWarrantyInformation();
//        this.shippingInformation = product.getShippingInformation();
//        this.availabilityStatus = product.getAvailabilityStatus();
//        this.reviews = product.getReviews();
//        this.returnPolicy = product.getReturnPolicy();
//        this.minimumOrderQuantity = product.getMinimumOrderQuantity();
//        this.meta = product.getMeta();
//        this.thumbnail = product.getThumbnail();

        // ✅ Extract first image from images list
        List<String> images = product.getImages();
        this.image = (images != null && !images.isEmpty()) ? images.get(0) : "https://via.placeholder.com/300";
    }

    // Getters and Setters (Optional if using @Data from Lombok)
}
