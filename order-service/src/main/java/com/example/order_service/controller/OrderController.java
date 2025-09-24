package com.example.order_service.controller;


import com.example.order_service.dto.OrderRequestDto;
import com.example.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@Slf4j
@RequiredArgsConstructor
@RefreshScope
public class OrderController {

    private final OrderService orderService;

    @Value("${my.variable}")
    private String myVariable;

    @GetMapping("/helloOrder")
    public String helloOrders()
    {
        return "Hello from Orders Service,my variable is :"+myVariable;
    }

    @PostMapping("/create-order")
    public ResponseEntity<OrderRequestDto> createOrder(@RequestBody OrderRequestDto orderRequestDto)
    {
        OrderRequestDto orderRequestDto1 = orderService.createOrder(orderRequestDto);
        return new ResponseEntity<>(orderRequestDto1,HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<OrderRequestDto>> getAllOrder()
    {
        log.info("Fetch All Orders");
        List<OrderRequestDto> orderRequestDtos = orderService.getAllOrders();

        return new ResponseEntity<>(orderRequestDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderRequestDto> getOrderById(@PathVariable Long id)
    {
        log.info("Fetching orders by id",id);
        OrderRequestDto orderRequestDto = orderService.getOrderById(id);

        return ResponseEntity.ok(orderRequestDto);

    }
}
