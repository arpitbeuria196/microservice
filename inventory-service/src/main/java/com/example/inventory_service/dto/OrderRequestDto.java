package com.example.inventory_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequestDto {
    private BigDecimal totalPrice;
    private List<OrderRequestItemDto> items;
}
