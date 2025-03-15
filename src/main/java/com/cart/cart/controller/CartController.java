package com.cart.cart.controller;

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

import com.cart.cart.dto.CartRequest;
import com.cart.cart.dto.CartResponse;
import com.cart.cart.service.CartService;
import com.cart.user.dto.UserCartResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/cart")
@Tag(name = "Cart", description = "Endpoints para gestionar el carrito de compras")
@SecurityRequirement(name = "Bearer Authentication")
public class CartController {

    @Autowired
    private CartService cartService;

    // Agregar un producto al carrito
    @PostMapping
    @Operation(summary = "Agregar un producto al carrito", description = "Agrega un producto al carrito de un usuario. Si el producto ya está en el carrito, se actualiza la cantidad.", responses = {
            @ApiResponse(responseCode = "200", description = "Producto agregado al carrito exitosamente"),
            @ApiResponse(responseCode = "400", description = "Cantidad inválida o stock insuficiente"),
            @ApiResponse(responseCode = "404", description = "Usuario o producto no encontrado") })
    public ResponseEntity<CartResponse> addToCart(@RequestBody CartRequest cartRequest) {
        CartResponse cartResponse = cartService.addToCart(cartRequest); // Cambiar a CartResponse
        return ResponseEntity.ok(cartResponse); // Devolver CartResponse
    }

    // Actualizar la cantidad de un producto en el carrito
    @PutMapping("/user/{userId}/product/{productId}")
    @Operation(summary = "Actualizar la cantidad de un producto en el carrito", description = "Actualiza la cantidad de un producto en el carrito de un usuario. Si la cantidad es 0, se elimina el producto.", responses = {
            @ApiResponse(responseCode = "200", description = "Cantidad actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Cantidad inválida o stock insuficiente"),
            @ApiResponse(responseCode = "404", description = "Usuario, producto o carrito no encontrado") })
    public ResponseEntity<CartResponse> updateCartItem(@PathVariable Long userId, @PathVariable Long productId,
            @RequestParam int quantity) {
        CartResponse updatedCart = cartService.updateCartItem(userId, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    // Eliminar un producto del carrito
    @DeleteMapping("/user/{userId}/product/{productId}")
    @Operation(summary = "Eliminar un producto del carrito", description = "Elimina un producto específico del carrito de un usuario.", responses = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario, producto o carrito no encontrado") })
    public ResponseEntity<Void> removeFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        cartService.removeFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{userId}")
    @Operation(summary = "Vaciar el carrito", description = "Elimina todos los productos del carrito de un usuario.", responses = {
            @ApiResponse(responseCode = "204", description = "Carrito vaciado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario o carrito no encontrado") })
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    // Obtener todos los productos en el carrito de un usuario
    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtener el carrito de un usuario", description = "Obtiene todos los productos en el carrito de un usuario.", responses = {
            @ApiResponse(responseCode = "200", description = "Carrito obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario o carrito no encontrado") })
    public ResponseEntity<UserCartResponse> getCartByUserId(@PathVariable Long userId) {
        UserCartResponse userCartResponse = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(userCartResponse);
    }
}