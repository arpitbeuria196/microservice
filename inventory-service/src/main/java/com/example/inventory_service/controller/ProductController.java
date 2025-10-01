package com.example.inventory_service.controller;


import com.example.inventory_service.clients.OrdersClient;
import com.example.inventory_service.dto.OrderRequestDto;
import com.example.inventory_service.dto.ProductDto;
import com.example.inventory_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("/product")
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final DiscoveryClient discoveryClient;

    private final RestClient restClient;

    private final OrdersClient ordersClient;

    @GetMapping("/fetchOrders")
    public String fetchFromOrderService(){

   //     List<ServiceInstance> orderService = discoveryClient.getInstances("order-service");

//      return   restClient.get()
//              .uri(orderService.get(0).getUri()+"/api/v1/order/helloOrder")
//              .retrieve()
//              .body(String.class);

        return ordersClient.helloOrders();
    }

    @GetMapping
    public ResponseEntity<List<ProductDto> >getAllInventories()
    {

        return new ResponseEntity<>(productService.getAllInventory(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id)
    {
        return new ResponseEntity<>(productService.getInventoryById(id),HttpStatus.OK);
    }

    @PutMapping("/reduce-stock")
    public ResponseEntity<Double> reduceStock(@RequestBody OrderRequestDto orderRequestDto)
    {
       Double price = productService.reduceStock(orderRequestDto);

       return new ResponseEntity<>(price,HttpStatus.OK);


    }

    @PostMapping(
            path = "/create-order-kafka",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<String> createOrderUsingKafka(@RequestBody OrderRequestDto orderRequestDto)
    {
        String orderRequestDto1 = productService.reduceStockUsingKafka(orderRequestDto);
        return new ResponseEntity<>(orderRequestDto1,HttpStatus.CREATED);
    }



}
