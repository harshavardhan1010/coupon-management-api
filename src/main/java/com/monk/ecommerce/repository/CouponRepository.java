package com.monk.ecommerce.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monk.ecommerce.entity.BxGyProduct;
import com.monk.ecommerce.entity.Coupon;
import com.monk.ecommerce.entity.Product;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
	
	Optional<Coupon> findByProduct(Product product);
	
	Optional<Coupon> findFirstByThreshold(BigDecimal sum);
	
	Optional<Coupon> findFirstByThresholdAndExpiryDateAfter(BigDecimal sum, LocalDateTime expiryDate);
	
	Optional<Coupon> findFirstByProductAndExpiryDateAfter(Product product,LocalDateTime expiryDate);

	
	boolean existsByBuyProducts(List<BxGyProduct> buyProducts);
    boolean existsByProduct(Product product);
    
}
