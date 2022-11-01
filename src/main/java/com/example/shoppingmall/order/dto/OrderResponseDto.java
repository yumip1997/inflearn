package com.example.shoppingmall.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderResponseDto {

    private List<OrderProductDto> orderProductDtoList;
    private int deliveryFee;
    private long orderAmount;
    private long payAmount;

    public static OrderResponseDto of(List<OrderProductDto> orderProductDtoList){
        long orderAmount = getTotalOrderAmount(orderProductDtoList);
        int deliveryFee = getDeliverFee(orderAmount);
        long payAmount = getPayAmount(orderAmount, deliveryFee);

        return OrderResponseDto.builder()
                .orderProductDtoList(orderProductDtoList)
                .orderAmount(orderAmount)
                .deliveryFee(deliveryFee)
                .payAmount(payAmount)
                .build();
    }

    private static long getTotalOrderAmount(List<OrderProductDto> orderProductDtoList){
        return orderProductDtoList.stream()
                .mapToLong(OrderProductDto::getOrderAmount)
                .sum();
    }

    private static int getDeliverFee(long orderAmount){
        return orderAmount < 50000 ? 2500 :0;
    }

    private static long getPayAmount(long orderAmount, int deliveryFee){
        return orderAmount + deliveryFee;
    }

}
