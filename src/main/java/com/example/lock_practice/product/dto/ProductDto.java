package com.example.lock_practice.product.dto;

import com.example.lock_practice.com.exception.BusinessException;
import com.example.lock_practice.product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.lock_practice.com.exception.message.stock.StockExceptionMessage.NOT_IN_STOCK_MESSAGE;

@NoArgsConstructor
@Getter
public class ProductDto {

    private String productId;

    private String productName;

    private long salePrice;

    private int quantity;

    public ProductDto(String productId, int quantity){
        this.productId = productId;
        this.quantity = quantity;
    }

    public ProductDto getProductDecreasedQuantity(String productId, int quantity){
        if(this.quantity < quantity){
            throw new BusinessException(NOT_IN_STOCK_MESSAGE);
        }
        return new ProductDto(productId, this.quantity - quantity);
    }
}
