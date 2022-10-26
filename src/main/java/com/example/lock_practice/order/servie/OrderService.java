package com.example.lock_practice.order.servie;

import com.example.lock_practice.order.dto.OrderProductDto;
import com.example.lock_practice.order.dto.OrderResponseDto;
import com.example.lock_practice.product.entity.Product;
import com.example.lock_practice.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final StockService stockService;

    public OrderResponseDto order(List<OrderProductDto> orderProductDtoList){
        return OrderResponseDto.of(stockService.decreaseStockList(orderProductDtoList));
    }

}
