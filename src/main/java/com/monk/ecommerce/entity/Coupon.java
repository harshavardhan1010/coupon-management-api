package com.monk.ecommerce.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.context.properties.bind.DefaultValue;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.monk.ecommerce.enums.CouponType;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "coupon")
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "coupon_type")
	@Enumerated(EnumType.STRING)
	private CouponType couponType;

	@Column(name = "expiry_date")
	private LocalDateTime expiryDate;

	@Column(name = "is_active")
	private Boolean active;

//	Cart-wise and product-wise properties

	private BigDecimal threshold;

	@OneToOne()
	private Product product;

	@Column(name = "disc_pct")
	private BigDecimal discountPercentage;

//	BX GY properties

//	@OneToMany(orphanRemoval = true)
//	@JoinColumn(name="coupon_id")
//	private List<BxGyProduct> buyProducts;
	
	@ManyToMany()
	@JoinTable(name = "bx_gy_coupon_buy_products", joinColumns = @JoinColumn(name = "coupon_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
	private List<BxGyProduct> buyProducts;

	@ManyToMany()
	@JoinTable(name = "bx_gy_coupon_get_products", joinColumns = @JoinColumn(name = "coupon_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
	private List<BxGyProduct> getProducts;

	@Column(name = "rep_limit")
	Integer repetitionLimit;
}
