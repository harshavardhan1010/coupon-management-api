package com.monk.ecommerce.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class Cart {
    private List<CartItem> items;
//    private BigDecimal totalPrice;
//    private BigDecimal totalDiscount;
//    private BigDecimal finalPrice;
}