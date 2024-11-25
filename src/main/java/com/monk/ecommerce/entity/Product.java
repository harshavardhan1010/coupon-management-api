package com.monk.ecommerce.entity;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="product")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name = "price")
	private BigDecimal price;
	
//	@ManyToMany
//	@JoinTable(name = "coupon_product", inverseJoinColumns = @JoinColumn(name = "coupon_id"), joinColumns = @JoinColumn(name = "product_id"))
//	private List<Coupon> coupon;
}
