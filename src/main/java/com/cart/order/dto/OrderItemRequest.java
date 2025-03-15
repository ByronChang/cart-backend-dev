package com.cart.order.dto;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Long productId; // ID del producto
    private int quantity; // Cantidad del producto
}