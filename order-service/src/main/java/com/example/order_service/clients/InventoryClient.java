package com.example.order_service.clients;


import com.example.order_service.dto.OrderRequestDto;
import feign.Capability;
import feign.micrometer.MicrometerCapability;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service",path = "/api/v1/product")
public interface InventoryClient {

    @PutMapping("/reduce-stock")
    Double reduceStock(@RequestBody OrderRequestDto orderRequestDto );

}
