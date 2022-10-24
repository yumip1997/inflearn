package com.example.lock_practice.order.facade;

import com.example.lock_practice.product.dto.ProductDto;
import com.example.lock_practice.product.service.ProductService;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

public class OrderFacade {

    private final ProductService productService;
    private final TransactionTemplate transactionTemplate;

    public OrderFacade(ProductService productService, PlatformTransactionManager transactionTemplate) {
        this.productService = productService;
        this.transactionTemplate = new TransactionTemplate(transactionTemplate);
    }

    public void order(List<ProductDto> productDtoList){
        transactionTemplate.executeWithoutResult((status) -> {
            doOrder(productDtoList);
        });
    }

    private void doOrder(List<ProductDto> productDtoList){
        //TODO 향후 주문 관련 테이블 insert, update, 결제 관련 메서드 추가
        decreaseStock(productDtoList);
    }

    private void decreaseStock(List<ProductDto> productDtoList){
        for (ProductDto productDto : productDtoList) {
            productService.decreaseStock(productDto.getProductId(), productDto.getQuantity());
        }
    }
}
