package com.example.shoppingmall.product.service;

import com.example.shoppingmall.com.exception.BusinessException;
import com.example.shoppingmall.com.exception.message.product.ProductExceptionMessage;
import com.example.shoppingmall.product.entity.Product;
import com.example.shoppingmall.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getProductList(){
        return productRepository.findAll();
    }

    public Product getProductById(String productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ProductExceptionMessage.PRODUCT_NOT_FOUND_MESSAGE));
    }

    public Product saveAndFlush(Product product){
        return productRepository.saveAndFlush(product);
    }

}
