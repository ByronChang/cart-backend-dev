package com.cart.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cart.order.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}

// import com.cart.entity.Order;
// import com.cart.entity.User;
// import org.springframework.data.jpa.repository.JpaRepository;
// import java.util.List;

// public interface OrderRepository extends JpaRepository<Order, Long> {

// // Buscar órdenes por usuario
// List<Order> findByUser(User user);

// // Buscar órdenes por usuario y estado
// List<Order> findByUserAndStatus(User user, String status);

// // Buscar órdenes por estado
// List<Order> findByStatus(String status);
// }