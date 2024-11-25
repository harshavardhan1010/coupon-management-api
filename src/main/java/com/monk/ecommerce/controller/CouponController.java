package com.monk.ecommerce.controller;

import java.util.List;

import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.monk.ecommerce.dto.Cart;
import com.monk.ecommerce.dto.CouponDTO;
import com.monk.ecommerce.dto.DiscountDTO;
import com.monk.ecommerce.entity.Coupon;
import com.monk.ecommerce.service.CouponService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
public class CouponController {

	private final CouponService couponService;

	@GetMapping("")
	public ResponseEntity<Page<?>> getCoupon(@RequestParam(value="page",defaultValue = "1")Integer page,@RequestParam(value="size",defaultValue = "10")Integer size){
		Page<CouponDTO> coupons = couponService.getAllCoupons(page,size);
		return ResponseEntity.status(HttpStatus.OK).body(coupons);
	}
	

	@GetMapping("/{id}")
	public ResponseEntity<?> getCoupon(@PathVariable("id") Long couponId){
		CouponDTO couponDTO = couponService.getCoupon(couponId);
		return ResponseEntity.status(HttpStatus.OK).body(couponDTO);
	}
	
	@PostMapping("")
	public ResponseEntity<?> createCoupon(@RequestBody CouponDTO coupondto){
		CouponDTO couponDto = couponService.createCoupon(coupondto);
		return ResponseEntity.ok(couponDto);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCoupon(@PathVariable("id") Long couponId){
		couponService.deleteCoupon(couponId);
		return ResponseEntity.ok().build();
	}
	
//	@PutMapping("")
//	public ResponseEntity<?> updateCoupon(@RequestBody CouponDTO coupondto){
//		CouponDTO coupon= couponService.updateCoupon(coupondto);
//	}
//	
	@PostMapping("/applicable-coupons")
	public ResponseEntity<?> applicableCoupons(@RequestBody Cart cart){
		List<DiscountDTO> coupons = couponService.getAllApplicableCoupons(cart);
		return ResponseEntity.ok(coupons);
	}
//	
//	@PostMapping("/apply-coupon/{id}")
//	public ResponseEntity<?> applicableCoupons(@PathVariable("couponId") Long couponId){
//		
//	}
}
