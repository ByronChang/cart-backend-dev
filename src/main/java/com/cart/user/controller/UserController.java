package com.cart.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cart.user.dto.UserResponse;
import com.cart.user.entity.User;
import com.cart.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "Endpoints para gestionar usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    // Método privado para construir un UserResponse a partir de un User
    private UserResponse buildUserResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getAddress(), user.getBirthDate());
    }

    // Endpoint para actualizar un usuario existente
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un usuario", description = "Actualiza los datos de un usuario existente", responses = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado") })
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(buildUserResponse(user));
    }

    // Endpoint para obtener un usuario por su email
    @GetMapping("/{email}")
    @Operation(summary = "Obtener un usuario por email", description = "Obtiene un usuario por su email", responses = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado") })
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(buildUserResponse(user));
    }

    // Endpoint para eliminar un usuario por su email
    @DeleteMapping("/{email}")
    @Operation(summary = "Eliminar un usuario por email", description = "Elimina un usuario por su email", responses = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado") })
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        userService.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }
}