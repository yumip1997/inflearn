package com.example.shoppingmall.order.servie;

import com.example.shoppingmall.stock.service.StockService;
import com.example.shoppingmall.order.dto.OrderProductDto;
import com.example.shoppingmall.order.dto.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final StockService stockService;

    @Transactional
    public OrderResponseDto order(List<OrderProductDto> orderProductDtoList){
        return OrderResponseDto.of(stockService.decreaseStockList(orderProductDtoList));
    }

}
