package com.example.lock_practice.stock.service;

import com.example.lock_practice.com.lock.annotation.RedissonLockAno;
import com.example.lock_practice.order.dto.OrderProductDto;
import com.example.lock_practice.product.entity.Product;
import com.example.lock_practice.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final ProductService productService;

    @RedissonLockAno
    public List<OrderProductDto> decreaseStockList(List<OrderProductDto> orderProductDtoList) {
        return orderProductDtoList.stream()
                .map(this::decreaseStock)
                .collect(Collectors.toList());
    }

    public OrderProductDto decreaseStock(OrderProductDto orderProductDto) {
        Product product = productService.getProductById(orderProductDto.getProductId());
        Product productDecreasedQuantity = product.ofDecreasedQuantity(orderProductDto.getQuantity());
        productService.saveAndFlush(productDecreasedQuantity);
        return orderProductDto;
    }

}
