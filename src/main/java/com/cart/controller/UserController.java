package com.cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cart.entity.User;
import com.cart.service.UserService;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Endpoint para registrar un nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        if (user.getBirthDate() == null) {
            throw new RuntimeException("La fecha de nacimiento no puede ser nula");
        }
        
        User newUser = userService.registerUser(user);
        return ResponseEntity.ok(newUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(user);
    }

    //Endpoint para obtener un usuario por su email
    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    // Endpoint para eliminar un usuario por su email
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        userService.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }
}