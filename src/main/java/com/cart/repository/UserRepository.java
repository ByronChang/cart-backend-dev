package com.cart.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cart.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // Buscar un usuario por su email
    Optional<User> findByEmail(String email);
    // Verificar si un usuario existe por su email
    boolean existsByEmail(String email);
    // Buscar usuarios por nombre de usuario (búsqueda parcial)
    List<User> findByUsernameContaining(String username);

    // Buscar usuarios por dirección (búsqueda parcial)
    List<User> findByAddressContaining(String address);

    // Buscar usuarios por fecha de nacimiento (rango de fechas)
    List<User> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);

    // Eliminar un usuario por su email
    void deleteByEmail(String email);
}