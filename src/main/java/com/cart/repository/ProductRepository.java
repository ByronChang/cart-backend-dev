package com.cart.repository;

import com.cart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Buscar productos por nombre (búsqueda parcial)
    List<Product> findByNameContaining(String name);

    // Buscar productos por rango de precios
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    // Buscar productos con stock disponible
    List<Product> findByStockGreaterThan(Integer stock);
}