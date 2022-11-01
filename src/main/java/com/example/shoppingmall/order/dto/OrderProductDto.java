package com.example.shoppingmall.order.dto;

import com.example.shoppingmall.com.lock.LockKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class OrderProductDto implements LockKey {

    private String productId;

    private String productName;

    private long salePrice;

    private int orderQuantity;

    public long getOrderAmount(){
        return salePrice * orderQuantity;
    }

    @Override
    @JsonIgnore
    public String getKey() {
        return this.toString();
    }
}
