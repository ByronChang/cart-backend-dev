package com.cart.cart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cart.cart.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUserId(Long userId);

    Optional<Cart> findByUserIdAndProductId(Long userId, Long productId);

    void deleteByUserId(Long userId);

    void deleteByUserIdAndProductId(Long userId, Long productId);
}
