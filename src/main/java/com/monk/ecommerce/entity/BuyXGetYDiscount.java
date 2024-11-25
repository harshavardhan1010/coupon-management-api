package com.monk.ecommerce.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

//@Entity
//@Table(name="bx_gy_discount")
//@Data
//@NoArgsConstructor
public class BuyXGetYDiscount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "rep_limit")
	Integer repetitionLimit; // How many times this offer can be applied in one cart
	
	@ManyToMany()
	@JoinTable(name = "bx_gy_buy_products", joinColumns = @JoinColumn(name = "coupon_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
	private Set<BxGyProduct> buyProducts = new HashSet<>(); // Products that need to be bought (X)

	@ManyToMany()
	@JoinTable(name = "bx_gy_get_products", joinColumns = @JoinColumn(name = "coupon_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
	private Set<BxGyProduct> freeProducts = new HashSet<>(); // Products that will be given free (Y)
}