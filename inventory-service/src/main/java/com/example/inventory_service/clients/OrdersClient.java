package com.example.inventory_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "order-service",path="/api/v1/order")
public interface OrdersClient {
    @GetMapping("/helloOrder")
     String helloOrders();
}
