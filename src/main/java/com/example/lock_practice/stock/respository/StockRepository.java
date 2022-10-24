package com.example.lock_practice.stock.respository;

import com.example.lock_practice.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<String, Product> {
}
