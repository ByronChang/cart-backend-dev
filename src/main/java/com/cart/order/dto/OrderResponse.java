package com.cart.order.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class OrderResponse {
    private Long id; // ID de la orden
    private Long userId; // ID del usuario
    private LocalDateTime orderDate; // Fecha de la orden
    private String shippingAddress; // Dirección de envío
    private String status; // Estado del pedido
    private List<OrderItemResponse> items; // Lista de productos en la orden
}