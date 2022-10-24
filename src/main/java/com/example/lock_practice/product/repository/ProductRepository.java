package com.example.lock_practice.product.repository;

import com.example.lock_practice.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {

}
