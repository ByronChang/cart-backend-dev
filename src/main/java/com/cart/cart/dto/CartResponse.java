package com.cart.cart.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartResponse {
    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private double productPrice;
    private int quantity;
    private LocalDateTime addedDate;
}