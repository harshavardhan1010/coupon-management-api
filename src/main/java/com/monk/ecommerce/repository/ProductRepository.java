package com.monk.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monk.ecommerce.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
