package com.cart.product.dto;

import lombok.Data;

@Data
public class ProductResponse {
    private Long id; // ID del producto
    private String name; // Nombre del producto
    private String description; // Descripción del producto
    private double price; // Precio del producto
    private String imageUrl;

    // Constructor con todos los campos
    public ProductResponse(Long id, String name, String description, double price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}