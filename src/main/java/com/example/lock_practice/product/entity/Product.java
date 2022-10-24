package com.example.lock_practice.product.entity;

import com.example.lock_practice.com.exception.BusinessException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

import static com.example.lock_practice.com.exception.message.stock.StockExceptionMessage.NOT_IN_STOCK_MESSAGE;

@Entity
@NoArgsConstructor
@Getter
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

    public Product getProductDecreasedQuantity(String productId, int quantity){
        if(this.quantity < quantity){
            throw new BusinessException(NOT_IN_STOCK_MESSAGE);
        }
        return new Product(productId, this.quantity - quantity);
    }
}
