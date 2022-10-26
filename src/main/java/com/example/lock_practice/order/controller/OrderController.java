package com.example.lock_practice.order.controller;

import com.example.lock_practice.order.dto.OrderProductDto;
import com.example.lock_practice.order.dto.OrderResponseDto;
import com.example.lock_practice.order.servie.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public OrderResponseDto order(@RequestBody List<OrderProductDto> orderProductDtoList){
        return orderService.order(orderProductDtoList);
    }
}
