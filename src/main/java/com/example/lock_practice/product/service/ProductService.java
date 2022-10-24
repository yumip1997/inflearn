package com.example.lock_practice.product.service;

import com.example.lock_practice.com.exception.BusinessException;
import com.example.lock_practice.com.exception.message.product.ProductExceptionMessage;
import com.example.lock_practice.product.dto.ProductDto;
import com.example.lock_practice.product.entity.Product;
import com.example.lock_practice.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.lock_practice.com.exception.message.stock.StockExceptionMessage.NOT_IN_STOCK_MESSAGE;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getProductList(){
        return productRepository.findAll();
    }

    public List<Product> getProductsByIds(List<String> productIds){

        return productRepository.findAllById(productIds);
    }
    public Product getProductById(String productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ProductExceptionMessage.PRODUCT_NOT_FOUND_MESSAGE));
    }

    public void decreaseStock(String productId, int quantity){
        Product product = getProductById(productId);
        Product productDecreasedQuantity = product.getProductDecreasedQuantity(productId, quantity);
        productRepository.saveAndFlush(productDecreasedQuantity);
    }

}
