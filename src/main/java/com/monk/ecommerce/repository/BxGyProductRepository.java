package com.monk.ecommerce.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monk.ecommerce.entity.BxGyProduct;
import com.monk.ecommerce.entity.Product;

public interface BxGyProductRepository extends JpaRepository<BxGyProduct, Long> {
	 Optional<BxGyProduct> findByProductAndQuantity(Product product, Integer quantity);
}
