package com.example.shoppingmall.product.controller;

import com.example.shoppingmall.com.dto.ResponseDto;
import com.example.shoppingmall.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product/all")
    public ResponseDto getAllProduct(){
        return ResponseDto.of(productService.getProductList());
    }

}
