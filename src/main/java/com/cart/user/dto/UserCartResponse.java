package com.cart.user.dto;

import java.util.List;

import com.cart.cart.dto.CartProductResponse;

import lombok.Data;

@Data
public class UserCartResponse {
    private UserResponse user; // Información del usuario (solo una vez)
    private List<CartProductResponse> products; // Lista de productos en el carrito

    // Constructor con todos los campos
    public UserCartResponse(UserResponse user, List<CartProductResponse> products) {
        this.user = user;
        this.products = products;
    }
}