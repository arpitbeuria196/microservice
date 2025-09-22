package com.example.order_service.service;


import com.example.order_service.clients.InventoryClient;
import com.example.order_service.dto.OrderRequestDto;
import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderItem;
import com.example.order_service.entity.OrderStatus;
import com.example.order_service.repository.OrderRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    
    
    private final OrderRepository orderRepository;

    private final InventoryClient inventoryClient;
    
    private ModelMapper modelMapper;
    
    public List<OrderRequestDto> getAllOrders()
    {
        log.info("Fetching All Orders");
        List<Order> orders = orderRepository.findAll();

        return orders.stream().map(order -> modelMapper.map(order, OrderRequestDto.class)).toList();
    }

    public OrderRequestDto getOrderById(Long id)
    {
        log.info("Fetching orders by id",id);
        Order order = orderRepository.findById(id).orElseThrow(()->new RuntimeException("Order Doesn't exist"));

        return modelMapper.map(order, OrderRequestDto.class);
    }


    @CircuitBreaker(name = "inventory", fallbackMethod = "fallback")
    @Retry(name = "inventory")
    @RateLimiter(name = "inventory")
    @Bulkhead(name = "inventory", type = Bulkhead.Type.SEMAPHORE)
    public OrderRequestDto createOrder(OrderRequestDto orderRequestDto) {

        Double price = inventoryClient.reduceStock(orderRequestDto);

        Order orders = modelMapper.map(orderRequestDto,Order.class);

        for (OrderItem orderItem : orders.getItems())
        {
            orderItem.setOrder(orders);
        }

        orders.setTotalPrice(BigDecimal.valueOf(price));

        orders.setOrderStatus(OrderStatus.SUCCEDED);

        Order savedOrder = orderRepository.save(orders);

        return modelMapper.map(savedOrder, OrderRequestDto.class);
    }

    private OrderRequestDto fallbackMethod(OrderRequestDto orderRequestDto,Throwable t)
    {
        log.warn("createOrder fallback triggered due to: {}", t.toString());

        return new OrderRequestDto();


    }
}
