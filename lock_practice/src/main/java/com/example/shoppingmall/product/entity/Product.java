package com.example.shoppingmall.product.entity;

import com.example.shoppingmall.com.exception.SoldOutException;
import com.example.shoppingmall.com.exception.message.stock.StockExceptionMessage;
import com.example.shoppingmall.order.dto.OrderProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Product{

    @Id
    private String productId;

    private String productName;

    private long salePrice;

    private int quantity;


    public Product ofDecreasedQuantity(int orderQuantity){
        if(this.quantity < orderQuantity){
            throw new SoldOutException(StockExceptionMessage.NOT_IN_STOCK_MESSAGE);
        }

        return Product.builder()
                .productId(this.productId)
                .productName(this.productName)
                .salePrice(this.salePrice)
                .quantity(this.quantity - orderQuantity)
                .build();
    }

    public OrderProductDto toOrderProductDto(int orderQuantity){
        return OrderProductDto.builder()
                .productId(this.productId)
                .productName(this.productName)
                .salePrice(this.salePrice)
                .orderQuantity(orderQuantity)
                .build();
    }
}
