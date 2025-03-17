package com.cart.cart.dto;

import java.time.LocalDateTime;

import com.cart.product.dto.ProductResponse;

import lombok.Data;

@Data
public class CartProductResponse {
    private ProductResponse product; // Información del producto
    private int quantity; // Cantidad del producto en el carrito
    private LocalDateTime addedDate; // Fecha en que se agregó el producto

    // Constructor con todos los campos
    public CartProductResponse(ProductResponse product, int quantity, LocalDateTime addedDate) {
        this.product = product;
        this.quantity = quantity;
        this.addedDate = addedDate;
    }
}