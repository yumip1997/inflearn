package com.example.shoppingmall.order.controller;

import com.example.shoppingmall.com.dto.ResponseDto;
import com.example.shoppingmall.order.dto.OrderProductDto;
import com.example.shoppingmall.order.servie.OrderService;
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
    public ResponseDto order(@RequestBody List<OrderProductDto> orderProductDtoList){
        return ResponseDto.of(orderService.order(orderProductDtoList));
    }
}
