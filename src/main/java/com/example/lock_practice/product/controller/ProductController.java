package com.example.lock_practice.product.controller;

import com.example.lock_practice.product.entity.Product;
import com.example.lock_practice.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product/all")
    public List<Product> getAllProduct(){
        return productService.getProductList();
    }

}
