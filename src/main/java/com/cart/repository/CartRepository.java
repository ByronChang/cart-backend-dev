package com.cart.repository;

import com.cart.entity.Cart;
import com.cart.entity.Product;
import com.cart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // Buscar carritos por usuario
    List<Cart> findByUser(User user);

    // Buscar carritos por usuario y producto
    Cart findByUserAndProduct(User user, Product product);

    // Eliminar carritos por usuario
    void deleteByUser(User user);
}