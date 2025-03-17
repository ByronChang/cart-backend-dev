package com.cart.order.dto;

import lombok.Data;

@Data
public class OrderItemResponse {
    private Long productId; // ID del producto
    private String productName; // Nombre del producto
    private int quantity; // Cantidad del producto
    private double price; // Precio del producto en el momento de la orden
}