package com.cart.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cart.order.entity.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    // Puedes agregar métodos personalizados aquí si es necesario
    List<OrderDetail> findByOrderId(Long orderId);
}
