package com.cart.order.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cart.cart.entity.Cart;
import com.cart.cart.repository.CartRepository;
import com.cart.exception.InvalidQuantityException;
import com.cart.exception.ResourceNotFoundException;
import com.cart.order.dto.OrderItemResponse;
import com.cart.order.dto.OrderRequest;
import com.cart.order.dto.OrderResponse;
import com.cart.order.entity.Order;
import com.cart.order.entity.OrderDetail;
import com.cart.order.entity.OrderStatusEnum;
import com.cart.order.repository.OrderDetailRepository;
import com.cart.order.repository.OrderRepository;
import com.cart.product.entity.Product;
import com.cart.product.repository.ProductRepository;
import com.cart.user.entity.User;
import com.cart.user.repository.UserRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    public OrderResponse createOrder(OrderRequest orderRequest) {
        // Buscar el usuario
        User user = userRepository.findById(orderRequest.getUserId()).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id: " + orderRequest.getUserId()));

        // Buscar los productos del carrito del usuario
        List<Cart> cartItems = cartRepository.findByUserId(user.getId());

        // Validar que el carrito no esté vacío
        if (cartItems.isEmpty()) {
            throw new InvalidQuantityException("Cart is empty for user with id: " + user.getId());
        }

        // Validar el stock de los productos en el carrito
        for (Cart cartItem : cartItems) {
            Product product = cartItem.getProduct();
            if (cartItem.getQuantity() > product.getStock()) {
                throw new InvalidQuantityException("Product " + product.getName()
                        + " exceeds available stock. Available stock: " + product.getStock());
            }
        }

        // Crear la orden
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setStatus(OrderStatusEnum.PENDING);
        orderRepository.save(order);

        // Crear los detalles de la orden y actualizar el stock de los productos
        List<OrderDetail> orderDetails = cartItems.stream().map(cartItem -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(cartItem.getProduct());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setPrice(cartItem.getProduct().getPrice());

            // Actualizar el stock del producto
            Product product = cartItem.getProduct();
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            return orderDetail;
        }).collect(Collectors.toList());

        orderDetailRepository.saveAll(orderDetails);

        // Vaciar el carrito del usuario
        cartRepository.deleteAll(cartItems);

        // Convertir la orden a OrderResponse
        return convertToOrderResponse(order, orderDetails);
    }

    private OrderResponse convertToOrderResponse(Order order, List<OrderDetail> orderDetails) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setUserId(order.getUser().getId());
        orderResponse.setOrderDate(order.getOrderDate());
        orderResponse.setShippingAddress(order.getShippingAddress());
        orderResponse.setStatus(order.getStatus().toString());

        List<OrderItemResponse> items = orderDetails.stream().map(orderDetail -> {
            OrderItemResponse item = new OrderItemResponse();
            item.setProductId(orderDetail.getProduct().getId());
            item.setProductName(orderDetail.getProduct().getName());
            item.setQuantity(orderDetail.getQuantity());
            item.setPrice(orderDetail.getPrice());
            return item;
        }).collect(Collectors.toList());

        orderResponse.setItems(items);
        return orderResponse;
    }

    public List<OrderResponse> getOrdersByUserId(Long userId) {
        // Buscar las órdenes del usuario
        List<Order> orders = orderRepository.findByUserId(userId);

        // Convertir cada orden a OrderResponse
        return orders.stream().map(order -> {
            List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(order.getId());
            return convertToOrderResponse(order, orderDetails);
        }).collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long orderId) {
        // Buscar la orden por su ID
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // Buscar los detalles de la orden
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);

        // Convertir la orden a OrderResponse
        return convertToOrderResponse(order, orderDetails);
    }

    public OrderResponse updateOrderStatus(Long orderId, String status) {
        // Buscar la orden por su ID
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // Validar el nuevo estado
        try {
            OrderStatusEnum newStatus = OrderStatusEnum.valueOf(status.toUpperCase());
            order.setStatus(newStatus);
        } catch (IllegalArgumentException e) {
            throw new InvalidQuantityException("Invalid order status: " + status);
        }

        // Guardar la orden actualizada
        orderRepository.save(order);

        // Buscar los detalles de la orden
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);

        // Convertir la orden a OrderResponse
        return convertToOrderResponse(order, orderDetails);
    }

    public void deleteOrder(Long orderId) {
        // Buscar la orden por su ID
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // Validar que la orden se pueda eliminar (por ejemplo, solo si está en estado
        // "Pendiente")
        if (order.getStatus() != OrderStatusEnum.PENDING) {
            throw new InvalidQuantityException("Order cannot be deleted because its status is: " + order.getStatus());
        }

        // Eliminar la orden
        orderRepository.delete(order);
    }
}

// package com.cart.service;

// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.stream.Collectors;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.cart.dto.OrderItemResponse;
// import com.cart.dto.OrderRequest;
// import com.cart.dto.OrderResponse;
// import com.cart.entity.Cart;
// import com.cart.entity.Order;
// import com.cart.entity.OrderDetail;
// import com.cart.entity.OrderStatus;
// import com.cart.entity.Product;
// import com.cart.entity.User;
// import com.cart.exception.InvalidQuantityException;
// import com.cart.exception.ResourceNotFoundException;
// import com.cart.repository.CartRepository;
// import com.cart.repository.OrderDetailRepository;
// import com.cart.repository.OrderRepository;
// import com.cart.repository.ProductRepository;
// import com.cart.repository.UserRepository;

// @Service
// public class OrderService {

// @Autowired
// private OrderRepository orderRepository;

// @Autowired
// private OrderDetailRepository orderDetailRepository;

// @Autowired
// private UserRepository userRepository;

// @Autowired
// private ProductRepository productRepository;

// @Autowired
// private CartRepository cartRepository;

// public OrderResponse createOrder(OrderRequest orderRequest) {
// // Buscar el usuario
// User user = userRepository.findById(orderRequest.getUserId()).orElseThrow(
// () -> new ResourceNotFoundException("User not found with id: " +
// orderRequest.getUserId()));

// // Buscar los productos del carrito del usuario
// List<Cart> cartItems = cartRepository.findByUserId(user.getId());

// // Validar que el carrito no esté vacío
// if (cartItems.isEmpty()) {
// throw new InvalidQuantityException("Cart is empty for user with id: " +
// user.getId());
// }

// // Validar el stock de los productos en el carrito
// for (Cart cartItem : cartItems) {
// Product product = cartItem.getProduct();
// if (cartItem.getQuantity() > product.getStock()) {
// throw new InvalidQuantityException("Product " + product.getName()
// + " exceeds available stock. Available stock: " + product.getStock());
// }
// }

// // Crear la orden
// Order order = new Order();
// order.setUser(user);
// order.setOrderDate(LocalDateTime.now());
// order.setShippingAddress(orderRequest.getShippingAddress());
// order.setStatus(OrderStatus.PENDING);
// orderRepository.save(order);

// // Crear los detalles de la orden y actualizar el stock de los productos
// List<OrderDetail> orderDetails = cartItems.stream().map(cartItem -> {
// OrderDetail orderDetail = new OrderDetail();
// orderDetail.setOrder(order);
// orderDetail.setProduct(cartItem.getProduct());
// orderDetail.setQuantity(cartItem.getQuantity());
// orderDetail.setPrice(cartItem.getProduct().getPrice());

// // Actualizar el stock del producto
// Product product = cartItem.getProduct();
// product.setStock(product.getStock() - cartItem.getQuantity());
// productRepository.save(product);

// return orderDetail;
// }).collect(Collectors.toList());

// orderDetailRepository.saveAll(orderDetails);

// // Vaciar el carrito del usuario
// cartRepository.deleteAll(cartItems);

// // Convertir la orden a OrderResponse
// return convertToOrderResponse(order, orderDetails);
// }

// private OrderResponse convertToOrderResponse(Order order, List<OrderDetail>
// orderDetails) {
// OrderResponse orderResponse = new OrderResponse();
// orderResponse.setId(order.getId());
// orderResponse.setUserId(order.getUser().getId());
// orderResponse.setOrderDate(order.getOrderDate());
// orderResponse.setShippingAddress(order.getShippingAddress());
// orderResponse.setStatus(order.getStatus().toString());

// List<OrderItemResponse> items = orderDetails.stream().map(orderDetail -> {
// OrderItemResponse item = new OrderItemResponse();
// item.setProductId(orderDetail.getProduct().getId());
// item.setProductName(orderDetail.getProduct().getName());
// item.setQuantity(orderDetail.getQuantity());
// item.setPrice(orderDetail.getPrice());
// return item;
// }).collect(Collectors.toList());

// orderResponse.setItems(items);
// return orderResponse;
// }
// }