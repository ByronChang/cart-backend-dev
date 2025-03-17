package com.cart.user.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserResponse {
    private Long id; // ID del usuario
    private String email; // Email del usuario
    private String address; // Dirección de envío
    private LocalDate birthDate; // Fecha de nacimiento
    private String username; // Nombre de usuario

    // Constructor con todos los campos
    public UserResponse(Long id, String email, String address, LocalDate birthDate, String username) {
        this.id = id;
        this.email = email;
        this.address = address;
        this.birthDate = birthDate;
        this.username = username;
    }
}