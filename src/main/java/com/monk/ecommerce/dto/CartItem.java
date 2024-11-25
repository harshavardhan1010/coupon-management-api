package com.monk.ecommerce.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartItem {
    private Long productId;
    private Integer quantity;
//    private BigDecimal price;
//    private BigDecimal totalDiscount;
}
