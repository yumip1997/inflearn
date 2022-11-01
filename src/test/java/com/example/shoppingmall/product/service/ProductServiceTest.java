package com.example.shoppingmall.product.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void product_exist_test(){
        Assertions.assertThat(productService.getProductById("768848")).isNotNull();
    }
}