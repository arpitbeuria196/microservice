package com.example.inventory_service.service;


import com.example.inventory_service.dto.OrderRequestDto;
import com.example.inventory_service.dto.OrderRequestItemDto;
import com.example.inventory_service.dto.ProductDto;
import com.example.inventory_service.entity.Product;
import com.example.inventory_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private  final ModelMapper modelMapper;
    public List<ProductDto> getAllInventory()
    {
        log.info("Fetching all the inventory");

        List<Product> inventories = productRepository.findAll();

        return inventories.stream().map((inventory)-> modelMapper.map(inventory, ProductDto.class)).toList();

    }

    public ProductDto getInventoryById(Long id)
    {
        log.info("Fetching Product with ID:{}",id);

        Product entity = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product doesn't exist with id " + id));


        return modelMapper.map(entity, ProductDto.class);
    }

    @Transactional
    public Double reduceStock(OrderRequestDto orderRequestDto) {

        log.info("Reducing the stocks");
        Double totalPrice = 0.0;
        for(OrderRequestItemDto orderRequestItemDto : orderRequestDto.getItems())
        {
            Long productId = orderRequestItemDto.getProductId();

            Integer quantity = orderRequestItemDto.getQuantity();
            Product product = productRepository.findById(productId).orElseThrow(()->new RuntimeException("Product Not Found"));
            totalPrice+= product.getPrice();

            if(quantity > product.getStock())
            {
                throw new RuntimeException("Qunatity is bigger than available Stock!");
            }

            product.setStock(product.getStock()-quantity);


        }
        return totalPrice;
    }
}
