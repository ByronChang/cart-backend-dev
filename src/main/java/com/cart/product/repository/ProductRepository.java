package com.cart.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cart.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Buscar productos por nombre (búsqueda parcial)
    List<Product> findByNameContaining(String name);

    // Buscar productos por rango de precios
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    // Buscar productos con stock disponible
    List<Product> findByStockGreaterThan(Integer stock);
}