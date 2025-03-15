package com.cart.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cart.order.dto.OrderRequest;
import com.cart.order.dto.OrderResponse;
import com.cart.order.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order", description = "Endpoints para gestionar las órdenes de compra")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    @Operation(summary = "Crear una orden de compra", description = "Crea una nueva orden de compra para un usuario.", responses = {
            @ApiResponse(responseCode = "200", description = "Orden creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Cantidad inválida o stock insuficiente"),
            @ApiResponse(responseCode = "404", description = "Usuario o producto no encontrado") })
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtener las órdenes de un usuario", description = "Obtiene todas las órdenes de compra de un usuario.", responses = {
            @ApiResponse(responseCode = "200", description = "Órdenes obtenidas exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado") })
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderResponse> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Obtener una orden por ID", description = "Obtiene una orden de compra por su ID.", responses = {
            @ApiResponse(responseCode = "200", description = "Orden obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada") })
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        OrderResponse orderResponse = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderResponse);
    }

    @PutMapping("/{orderId}/status")
    @Operation(summary = "Actualizar el estado de una orden", description = "Actualiza el estado de una orden de compra.", responses = {
            @ApiResponse(responseCode = "200", description = "Estado de la orden actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Estado inválido"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada") })
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        OrderResponse orderResponse = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(orderResponse);
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "Eliminar una orden", description = "Elimina una orden de compra.", responses = {
            @ApiResponse(responseCode = "204", description = "Orden eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada") })
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}