package com.cart.order.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderRequest {
    private Long userId; // ID del usuario
    private String shippingAddress; // Dirección de envío
    private List<OrderItemRequest> items; // Lista de productos en la orden
}