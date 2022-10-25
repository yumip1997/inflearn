package com.example.lock_practice.stock.service;

import com.example.lock_practice.com.lock.annotation.RedissonLockAno;
import com.example.lock_practice.product.dto.ProductDto;
import com.example.lock_practice.product.entity.Product;
import com.example.lock_practice.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final ProductService productService;

    @RedissonLockAno
    public List<Product> decreaseStockList(List<ProductDto> productDtoList){
        return productDtoList.stream()
                .map(this::decreaseStock)
                .collect(Collectors.toList());
    }

    public Product decreaseStock(ProductDto productDto){
        Product product = productService.getProductById(productDto.getProductId());
        Product productDecreasedQuantity = product.ofDecreasedQuantity(productDto.getQuantity());

        return productService.saveAndFlush(productDecreasedQuantity);
    }

}
