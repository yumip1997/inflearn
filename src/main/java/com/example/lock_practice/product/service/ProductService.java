package com.example.lock_practice.product.service;

import com.example.lock_practice.com.exception.BusinessException;
import com.example.lock_practice.com.exception.message.product.ProductExceptionMessage;
import com.example.lock_practice.product.entity.Product;
import com.example.lock_practice.product.repository.ProductRepository;
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
