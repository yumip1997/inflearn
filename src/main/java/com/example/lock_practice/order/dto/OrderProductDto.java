package com.example.lock_practice.order.dto;

import com.example.lock_practice.com.exception.BusinessException;
import com.example.lock_practice.com.lock.LockKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.lock_practice.com.exception.message.stock.StockExceptionMessage.NOT_IN_STOCK_MESSAGE;

@NoArgsConstructor
@Getter
public class OrderProductDto implements LockKey {

    private String productId;

    private String productName;

    private long salePrice;

    private int quantity;

    public OrderProductDto(String productId, int quantity){
        this.productId = productId;
        this.quantity = quantity;
    }

    public OrderProductDto ofDecreasedQuantity(String productId, int quantity){
        if(this.quantity < quantity){
            throw new BusinessException(NOT_IN_STOCK_MESSAGE);
        }
        return new OrderProductDto(productId, this.quantity - quantity);
    }

    public long getOrderAmount(){
        return salePrice * quantity;
    }

    @Override
    @JsonIgnore
    public String getKey() {
        return this.toString();
    }
}
