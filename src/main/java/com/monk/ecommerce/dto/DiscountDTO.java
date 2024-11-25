package com.monk.ecommerce.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.monk.ecommerce.enums.CouponType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DiscountDTO {
	private Long couponId;
	private CouponType couponType;
	private BigDecimal discount; 
	private BigDecimal totalDiscount;
}
