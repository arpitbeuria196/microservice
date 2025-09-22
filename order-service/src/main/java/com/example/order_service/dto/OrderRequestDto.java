package com.example.order_service.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequestDto {
    private Long id;
    private BigDecimal totalPrice;
    private List<OrderRequestItemDto> items;
}
