package com.example.lock_practice.order.servie;

import com.example.lock_practice.product.dto.ProductDto;
import com.example.lock_practice.product.entity.Product;
import com.example.lock_practice.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final StockService stockService;

    @Transactional
    public List<Product> order(List<ProductDto> productDtoList){
        return stockService.decreaseStockList(productDtoList);
    }

}
