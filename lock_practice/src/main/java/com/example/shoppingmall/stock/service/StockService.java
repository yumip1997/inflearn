package com.example.shoppingmall.stock.service;

import com.example.shoppingmall.com.lock.annotation.RedissonLockAno;
import com.example.shoppingmall.product.service.ProductService;
import com.example.shoppingmall.order.dto.OrderProductDto;
import com.example.shoppingmall.product.entity.Product;
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

        Product decreasedQuantity = product.ofDecreasedQuantity(orderProductDto.getOrderQuantity());
        productService.saveAndFlush(decreasedQuantity);

        return product.toOrderProductDto(orderProductDto.getOrderQuantity());
    }

}
