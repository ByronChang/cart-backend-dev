package com.cart.cart.service;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cart.cart.dto.CartProductResponse;
import com.cart.cart.dto.CartRequest;
import com.cart.cart.dto.CartResponse;
import com.cart.cart.entity.Cart;
import com.cart.cart.repository.CartRepository;
import com.cart.exception.InvalidQuantityException;
import com.cart.exception.ResourceNotFoundException;
import com.cart.product.dto.ProductResponse;
import com.cart.product.entity.Product;
import com.cart.product.repository.ProductRepository;
import com.cart.user.dto.UserCartResponse;
import com.cart.user.dto.UserResponse;
import com.cart.user.entity.User;
import com.cart.user.repository.UserRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    // Agregar un producto al carrito
    public CartResponse addToCart(CartRequest cartRequest) {
        // Buscar el usuario y el producto por sus IDs
        User user = userRepository.findById(cartRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + cartRequest.getUserId()));
        Product product = productRepository.findById(cartRequest.getProductId()).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id: " + cartRequest.getProductId()));

        // Validar que la cantidad solicitada no sea menor o igual a 0
        if (cartRequest.getQuantity() <= 0) {
            throw new InvalidQuantityException("Quantity must be greater than 0");
        }

        // Validar que la cantidad solicitada no supere el stock disponible
        if (cartRequest.getQuantity() > product.getStock()) {
            throw new InvalidQuantityException(
                    "Requested quantity exceeds available stock. Available stock: " + product.getStock());
        }

        // Verificar si el producto ya está en el carrito del usuario
        Optional<Cart> existingCartItem = cartRepository.findByUserIdAndProductId(cartRequest.getUserId(),
                cartRequest.getProductId());
        if (existingCartItem.isPresent()) {
            // Si el producto ya está en el carrito, validar que la cantidad total no supere
            // el stock
            Cart existingCart = existingCartItem.get();
            int totalQuantity = existingCart.getQuantity() + cartRequest.getQuantity();
            if (totalQuantity > product.getStock()) {
                throw new InvalidQuantityException(
                        "Total quantity exceeds available stock. Available stock: " + product.getStock());
            }

            // Actualizar la cantidad del producto en el carrito
            existingCart.setQuantity(totalQuantity);
            cartRepository.save(existingCart);
            return convertToCartResponse(existingCart);
        } else {
            // Si el producto no está en el carrito, agregarlo
            Cart newCartItem = new Cart();
            newCartItem.setUser(user);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(cartRequest.getQuantity());
            newCartItem.setAddedDate(new Date());
            cartRepository.save(newCartItem);
            return convertToCartResponse(newCartItem);
        }
    }

    private CartResponse convertToCartResponse(Cart cart) {
        return new CartResponse(cart.getId(), cart.getUser().getId(), cart.getProduct().getId(),
                cart.getProduct().getName(), cart.getProduct().getPrice(), cart.getQuantity(),
                cart.getAddedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
    }

    // Actualizar la cantidad de un producto en el carrito
    public CartResponse updateCartItem(Long userId, Long productId, int quantity) {
        // Validar que la cantidad sea mayor que 0
        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity must be greater than 0");
        }

        // Buscar el carrito del usuario
        List<Cart> userCarts = cartRepository.findByUserId(userId);
        if (userCarts.isEmpty()) {
            throw new ResourceNotFoundException("Cart not found for user with id: " + userId);
        }

        // Asumimos que un usuario tiene solo un carrito
        Cart cart = userCarts.get(0);

        // Verificar que el producto esté en el carrito
        if (!cart.getProduct().getId().equals(productId)) {
            throw new ResourceNotFoundException("Product not found in cart with id: " + productId);
        }

        // Validar que la cantidad no supere el stock disponible
        if (quantity > cart.getProduct().getStock()) {
            throw new InvalidQuantityException(
                    "Requested quantity exceeds available stock. Available stock: " + cart.getProduct().getStock());
        }

        // Actualizar la cantidad del producto
        cart.setQuantity(quantity);

        // Guardar el carrito actualizado
        Cart updatedCart = cartRepository.save(cart);

        // Convertir a CartResponse y devolver
        return new CartResponse(updatedCart.getId(), updatedCart.getUser().getId(), updatedCart.getProduct().getId(),
                updatedCart.getProduct().getName(), updatedCart.getProduct().getPrice(), updatedCart.getQuantity(),
                updatedCart.getAddedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
    }

    // public CartResponse updateCartItem(Long cartId, Long productId, int quantity)
    // {
    // // Validar que la cantidad sea mayor que 0
    // if (quantity <= 0) {
    // throw new InvalidQuantityException("Quantity must be greater than 0");
    // }

    // // Buscar el carrito por su ID
    // Cart cart = cartRepository.findById(cartId)
    // .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " +
    // cartId));

    // // Verificar que el producto esté en el carrito
    // if (!cart.getProduct().getId().equals(productId)) {
    // throw new ResourceNotFoundException("Product not found in cart with id: " +
    // productId);
    // }

    // // Validar que la cantidad no supere el stock disponible
    // if (quantity > cart.getProduct().getStock()) {
    // throw new InvalidQuantityException("Requested quantity exceeds available
    // stock. Available stock: " + cart.getProduct().getStock());
    // }

    // // Actualizar la cantidad del producto
    // cart.setQuantity(quantity);

    // // Guardar el carrito actualizado
    // Cart updatedCart = cartRepository.save(cart);

    // // Convertir a CartResponse y devolver
    // return new CartResponse(
    // updatedCart.getId(),
    // updatedCart.getUser().getId(),
    // updatedCart.getProduct().getId(),
    // updatedCart.getProduct().getName(),
    // updatedCart.getProduct().getPrice(),
    // updatedCart.getQuantity(),
    // updatedCart.getAddedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    // );
    // }

    // Eliminar un producto del carrito
    public void removeFromCart(Long userId, Long productId) {
        // Buscar el carrito del usuario que contiene el producto
        Optional<Cart> cartItem = cartRepository.findByUserIdAndProductId(userId, productId);
        if (cartItem.isPresent()) {
            // Si el producto está en el carrito, eliminarlo
            cartRepository.delete(cartItem.get());
        } else {
            // Si el producto no está en el carrito, lanzar una excepción
            throw new ResourceNotFoundException("Product not found in cart for user with id: " + userId);
        }
    }

    public void clearCart(Long userId) {
        // Buscar todos los productos en el carrito del usuario
        List<Cart> userCartItems = cartRepository.findByUserId(userId);
        if (!userCartItems.isEmpty()) {
            // Si hay productos en el carrito, eliminarlos todos
            cartRepository.deleteAll(userCartItems);
        } else {
            // Si el carrito está vacío, lanzar una excepción
            throw new ResourceNotFoundException("Cart is already empty for user with id: " + userId);
        }
    }

    // Obtener todos los productos en el carrito de un usuario
    public UserCartResponse getCartByUserId(Long userId) {
        // Obtener el usuario
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Obtener los items del carrito para el usuario
        List<Cart> cartItems = cartRepository.findByUserId(userId);

        // Convertir los items del carrito a CartProductResponse
        List<CartProductResponse> products = cartItems.stream()
                .map(cart -> new CartProductResponse(
                        new ProductResponse(cart.getProduct().getId(), cart.getProduct().getName(),
                                cart.getProduct().getDescription(), cart.getProduct().getPrice()),
                        cart.getQuantity(),
                        cart.getAddedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()))
                .collect(Collectors.toList());

        // Crear y devolver el UserCartResponse
        return new UserCartResponse(
                new UserResponse(user.getId(), user.getEmail(), user.getAddress(), user.getBirthDate()), products);
    }

}