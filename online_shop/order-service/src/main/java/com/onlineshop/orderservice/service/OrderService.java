package com.onlineshop.orderservice.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.onlineshop.orderservice.dto.OrderItemDto;
import com.onlineshop.orderservice.dto.OrderRequest;
import com.onlineshop.orderservice.dto.OrderResponse;
import com.onlineshop.orderservice.model.Order;
import com.onlineshop.orderservice.model.OrderItem;
import com.onlineshop.orderservice.model.OrderStatus;
import com.onlineshop.orderservice.repository.OrderRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {

    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    private RabbitTemplate rabbitTemplate;
    static final String exchangeName = "exchange";
    static final String routingKey = "cancel_order_key";

    public void createOrder(OrderRequest createOrderDto, Jwt jwt) {
        Order order = new Order();
        String userId = jwt.getClaimAsString("sub");
        order.setUserId(userId);

        List<OrderItem> orderItems = createOrderDto.getOrderItems().stream().map(orderItemDto -> {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(orderItemDto.getProductId());
            item.setQuantity(orderItemDto.getQuantity());
            item.setPrice(orderItemDto.getPrice());
            return item;
        }).toList();

        order.setOrderItems(orderItems);
        order.setTotalPrice(
                orderItems.stream().map(orderItem -> orderItem.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add));
        var savedOrder = orderRepository.save(order);
        try {
            sendReservationRequest(savedOrder, jwt);
        } catch (Exception e) {
            log.info("Reservation request failed");
            orderRepository.deleteById(savedOrder.getId());
            throw e;
        }
    }

    public Set<OrderResponse> getAllOrders() {
        return ((Collection<Order>) orderRepository.findAll()).stream().map(
                order -> {
                    OrderResponse response = new OrderResponse();
                    response.setId(order.getId());
                    response.setUserId(order.getUserId());
                    response.setStatus(order.getStatus());
                    response.setOrderItems(order.getOrderItems());
                    response.setTotalPrice(order.getTotalPrice());
                    return response;
                })
                .collect(Collectors.toSet());
    }

    private ResponseEntity<String> sendReservationRequest(Order order, Jwt jwt) {
        String url = "http://INVENTORY/api/v1/inventory/handle-order";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", "Bearer " + jwt.getTokenValue());

        HttpEntity<Long> entity = new HttpEntity<>(order.getId(), headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response;
    }

    public Set<OrderResponse> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(nullableOrder -> nullableOrder.orElse(null))
                .filter(order -> order != null)
                .map(
                        order -> {
                            OrderResponse response = new OrderResponse();
                            response.setId(order.getId());
                            response.setUserId(order.getUserId());
                            response.setStatus(order.getStatus());
                            response.setOrderItems(order.getOrderItems());
                            response.setTotalPrice(order.getTotalPrice());
                            return response;
                        })
                .collect(Collectors.toSet());
    }

    public OrderResponse getOrderById(Long id, String userId) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Order not found");
        }

        if (!userId.equals(order.getUserId())) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Access denied");
        }

        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUserId(order.getUserId());
        response.setStatus(order.getStatus());
        response.setOrderItems(order.getOrderItems());
        response.setTotalPrice(order.getTotalPrice());
        return response;
    }

    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

    public void updateOrderStatus(Long id, OrderStatus status, String userId) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Order not found");
        }

        if (!userId.equals(order.getUserId())) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Access denied");
        }
        order.setStatus(status);
        orderRepository.save(order);
    }

    public void cancelOrder(Long id, String jwt) {
        var order = getOrderById(id, jwt);
        if (order.getStatus().equals(OrderStatus.CANCELLED)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Order already cancelled");
        }
        updateOrderStatus(id, OrderStatus.CANCELLED, jwt);
        OrderRequest orderRequest = OrderRequest.builder().orderItems(
                order.getOrderItems().stream()
                        .map(orderItem -> OrderItemDto.builder().productId(orderItem.getProductId())
                                .quantity(orderItem.getQuantity()).price(orderItem.getPrice()).build())
                        .toList())
                .build();
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(exchangeName, routingKey, orderRequest);
    }

}
