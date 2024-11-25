package com.monk.ecommerce.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.monk.ecommerce.enums.CouponType;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CouponDTO {
	private Long couponId;

	private CouponType couponType;

	private LocalDateTime expiryDate;

	private Boolean active;

//	Cart-wise and product-wise properties

	private BigDecimal threshold;

	private Long productId;

	private BigDecimal discountPercentage;

//	BX GY properties

	private List<BxGyProductDTO> buyProducts;

	private List<BxGyProductDTO> getProducts;

	Integer repetitionLimit;
}
