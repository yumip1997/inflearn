package com.example.lock_practice.product.entity;

import com.example.lock_practice.com.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

import static com.example.lock_practice.com.exception.message.stock.StockExceptionMessage.NOT_IN_STOCK_MESSAGE;

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

    public Product(String productId, int quantity){
        this.productId = productId;
        this.quantity = quantity;
    }

    public Product ofDecreasedQuantity(int quantity){
        if(this.quantity < quantity){
            throw new BusinessException(NOT_IN_STOCK_MESSAGE);
        }

        return Product.builder()
                .productId(this.productId)
                .productName(this.productName)
                .salePrice(this.salePrice)
                .quantity(this.quantity - quantity)
                .build();
    }
}
