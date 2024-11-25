package com.monk.ecommerce.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.monk.ecommerce.dto.BxGyProductDTO;
import com.monk.ecommerce.dto.Cart;
import com.monk.ecommerce.dto.CartItem;
import com.monk.ecommerce.dto.CouponDTO;
import com.monk.ecommerce.dto.DiscountDTO;
import com.monk.ecommerce.entity.BxGyProduct;
import com.monk.ecommerce.entity.Coupon;
import com.monk.ecommerce.entity.Product;
import com.monk.ecommerce.enums.CouponType;
import com.monk.ecommerce.exception.GenericApplicationException;
import com.monk.ecommerce.repository.BxGyProductRepository;
import com.monk.ecommerce.repository.CouponRepository;
import com.monk.ecommerce.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponService {
	private final CouponRepository couponRepo;
	private final ProductRepository productRepo;
	private final BxGyProductRepository bxGyProductRepo;

	public Page<CouponDTO> getAllCoupons(int page, int size) {
		Pageable pageAble = PageRequest.of(page - 1, size);
		Page<Coupon> pageContent = couponRepo.findAll(pageAble);
		List<Coupon> coupons = pageContent.getContent();
		List<CouponDTO> couponDtos = convertCouponDTOList(coupons);
		return new PageImpl<>(couponDtos, pageAble, couponDtos.size());
	}

	public CouponDTO getCoupon(Long id) {
		Coupon coupon = couponRepo.findById(id)
				.orElseThrow(() -> GenericApplicationException.builder().summary("Coupon not found")
						.detail(String.format("Coupon with id %d does not exist", id)).statusCode(HttpStatus.NOT_FOUND)
						.build());
		return convertCouponToCouponDTO(coupon);
	}

	@Transactional
	public CouponDTO createCoupon(CouponDTO coupondto) {
		Coupon coupon = convertCouponDtoToCoupon(coupondto);
		return convertCouponToCouponDTO(coupon);
	}

	private Coupon convertCouponDtoToCoupon(CouponDTO coupondto) {
		Coupon coupon = new Coupon();
		coupon.setCouponType(coupondto.getCouponType());
		coupon.setExpiryDate(coupondto.getExpiryDate());
		coupon.setActive(coupondto.getActive());

		if (coupondto.getCouponType() == CouponType.BXGY) {
			// Check if buyProducts and getProducts overlap
			boolean isPresent = coupondto.getGetProducts().stream().anyMatch(coupondto.getBuyProducts()::contains);
			if (isPresent) {
				throw GenericApplicationException.builder().summary("Buy and get products should be different").build();
			}

			// Map buyProducts and getProducts
			List<BxGyProduct> buyProducts = coupondto.getBuyProducts().stream()
					.map(this::convertBxGyProductDtoToBxGyProduct).collect(Collectors.toList());

			List<BxGyProduct> getProducts = coupondto.getGetProducts().stream()
					.map(this::convertBxGyProductDtoToBxGyProduct).collect(Collectors.toList());

			// Check for duplicate buyProducts in the repository
//	        if (couponRepo.existsByBuyProducts(buyProducts)) {
//	            throw GenericApplicationException.builder()
//	                .summary("Coupon already exists for the specified buy products")
//	                .statusCode(HttpStatus.CONFLICT)
//	                .build();
//	        }

//			if (checkIfBxGyProductsExist(coupondto.getBuyProducts())) {
//				throw GenericApplicationException.builder()
//						.summary("Coupon already exists for the specified buy products").statusCode(HttpStatus.CONFLICT)
//						.build();
//			}

			// Set properties and save
			coupon.setRepetitionLimit(coupondto.getRepetitionLimit());
			coupon.setBuyProducts(buyProducts);
			coupon.setGetProducts(getProducts);

			// Save coupon and associated products
			couponRepo.save(coupon);
			buyProducts.forEach(bxGyProductRepo::save);
			getProducts.forEach(bxGyProductRepo::save);

			return coupon;
		} else if (coupondto.getCouponType() == CouponType.PRODUCT_WISE) {
			// Handle PRODUCT_WISE type
			Product product = productRepo.findById(coupondto.getProductId())
					.orElseThrow(() -> GenericApplicationException.builder().summary("Product Not Found")
							.detail(String.format("Product with id %d does not exist", coupondto.getProductId()))
							.build());

			if (couponRepo.existsByProduct(product)) {
				throw GenericApplicationException.builder().summary("Coupon already exists for this product")
						.statusCode(HttpStatus.CONFLICT).build();
			}

			coupon.setDiscountPercentage(coupondto.getDiscountPercentage());
			coupon.setProduct(product);
			return couponRepo.save(coupon);
		} else {
			// Handle other coupon types
			coupon.setDiscountPercentage(coupondto.getDiscountPercentage());
			coupon.setThreshold(coupondto.getThreshold());
			return couponRepo.save(coupon);
		}
	}

//	public boolean checkIfBxGyProductsExist(List<BxGyProductDTO> buyPairs) {
//		// Convert BxGyProductDTO list into Object[] list
//		List<BxGyProduct> buyProducts = new ArrayList<>();
//		for (BxGyProductDTO buyPair : buyPairs) {
//			Product product = productRepo.findById(buyPair.getProductId()).orElseThrow(() -> GenericApplicationException.builder().summary("Product Not Found")
//					.detail(String.format("Product with id %d does not exist", buyPair.getProductId()))
//					.build());
//			bxGyProductRepo.findByProductAndQuantity(product, buyPair.getQuantity()).ifPresent(buyProducts::add);
//		}
//		return couponRepo.existsByBuyProducts(buyProducts);
//	}

	/*
	 * private Coupon convertCouponDtoToCoupon(CouponDTO coupondto) { Coupon coupon
	 * = new Coupon(); coupon.setCouponType(coupondto.getCouponType());
	 * coupon.setExpiryDate(coupondto.getExpiryDate());
	 * coupon.setActive(coupondto.getActive()); if (coupondto.getCouponType() ==
	 * CouponType.BXGY) { boolean isPresent =
	 * coupondto.getGetProducts().stream().anyMatch(coupondto.getBuyProducts()::
	 * contains); if (isPresent) { throw GenericApplicationException.builder().
	 * summary("Buy and get products should be different").build(); }
	 * List<BxGyProduct> buyProducts = new ArrayList<>(); buyProducts =
	 * coupondto.getBuyProducts().stream().map(this::
	 * convertBxGyProductDtoToBxGyProduct) .collect(Collectors.toList());
	 * List<BxGyProduct> getProducts = new ArrayList<>(); getProducts =
	 * coupondto.getGetProducts().stream().map(this::
	 * convertBxGyProductDtoToBxGyProduct) .collect(Collectors.toList());
	 * coupon.setRepetitionLimit(coupondto.getRepetitionLimit());
	 * couponRepo.save(coupon); buyProducts.forEach(bxGyProductRepo::save);
	 * coupon.setBuyProducts(buyProducts);
	 * getProducts.forEach(bxGyProductRepo::save);
	 * coupon.setGetProducts(getProducts);
	 * couponRepo.findByBuyProductsIn(buyProducts).ifPresent(v -> {
	 * System.out.println("found it"); throw GenericApplicationException.builder().
	 * summary("Coupon Already exists for this buy products")
	 * .detail(String.format("Coupon id for this product is %d",
	 * v.getId())).statusCode(HttpStatus.CONFLICT).build(); }); return
	 * couponRepo.save(coupon); } else if (coupondto.getCouponType() ==
	 * CouponType.PRODUCT_WISE) { Product product =
	 * productRepo.findById(coupondto.getProductId()).orElseThrow(() -> { return
	 * GenericApplicationException.builder().summary("Product Not Found")
	 * .summary(String.format("Product with %d not exists",
	 * coupondto.getProductId())).build(); });
	 * couponRepo.findByProduct(product).ifPresent(v -> { throw
	 * GenericApplicationException.builder().
	 * summary("Coupon Already exists for this product")
	 * .detail(String.format("Coupon id for this product is %d",
	 * v.getProduct().getId())).statusCode(HttpStatus.CONFLICT).build(); });
	 * coupon.setDiscountPercentage(coupondto.getDiscountPercentage()); return
	 * couponRepo.save(coupon); } else {
	 * coupon.setDiscountPercentage(coupondto.getDiscountPercentage());
	 * coupon.setThreshold(coupondto.getThreshold()); return
	 * couponRepo.save(coupon); } }
	 */

	private BxGyProduct convertBxGyProductDtoToBxGyProduct(BxGyProductDTO bxGyProductDTO) {
		Product product = productRepo.findById(bxGyProductDTO.getProductId()).orElseThrow(() -> {
			return GenericApplicationException.builder().summary("Product Not Found")
					.summary(String.format("Product with %d not exists", bxGyProductDTO.getProductId())).build();
		});
		BxGyProduct buyProduct = BxGyProduct.builder().product(product).quantity(bxGyProductDTO.getQuantity()).build();
		return buyProduct;
	}

	private List<CouponDTO> convertCouponDTOList(List<Coupon> couponEntites) {
		List<CouponDTO> couponDtos = new ArrayList<>();
		for (Coupon coupon : couponEntites) {
			CouponDTO coupondto = convertCouponToCouponDTO(coupon);
			couponDtos.add(coupondto);
		}
		return couponDtos;
	}

	private CouponDTO convertCouponToCouponDTO(Coupon coupon) {
		CouponDTO coupondto = new CouponDTO();
		coupondto.setCouponId(coupon.getId());
		coupondto.setCouponType(coupon.getCouponType());
		coupondto.setExpiryDate(coupon.getExpiryDate());
		coupondto.setActive(coupon.getActive());
		if (coupon.getCouponType() == CouponType.BXGY) {
			List<BxGyProduct> buyProducts = coupon.getBuyProducts();
			List<BxGyProductDTO> buyProductsDTO = new ArrayList<>();
			for (BxGyProduct buyProduct : buyProducts) {
				BxGyProductDTO buyProductDTO = new BxGyProductDTO();
				buyProductDTO.setProductId(buyProduct.getProduct().getId());
				buyProductDTO.setQuantity(buyProduct.getQuantity());
				buyProductsDTO.add(buyProductDTO);
			}
			List<BxGyProductDTO> getProductsDTO = new ArrayList<>();
			List<BxGyProduct> getProducts = coupon.getGetProducts();
			for (BxGyProduct getProduct : getProducts) {
				BxGyProductDTO getProductDTO = new BxGyProductDTO();
				getProductDTO.setProductId(getProduct.getProduct().getId());
				getProductDTO.setQuantity(getProduct.getQuantity());
				getProductsDTO.add(getProductDTO);
			}
			coupondto.setBuyProducts(buyProductsDTO);
			coupondto.setGetProducts(getProductsDTO);
			coupondto.setRepetitionLimit(coupon.getRepetitionLimit());
		} else if (coupon.getCouponType() == CouponType.PRODUCT_WISE) {
			coupondto.setProductId(coupon.getProduct().getId());
			coupondto.setDiscountPercentage(coupon.getDiscountPercentage());
		} else if (coupon.getCouponType() == CouponType.CART_WISE) {
			coupondto.setThreshold(coupon.getThreshold());
			coupondto.setDiscountPercentage(coupon.getDiscountPercentage());
		}
		return coupondto;
	}

	@Transactional
	public void deleteCoupon(Long couponId) {
		// TODO Auto-generated method stub
		couponRepo.deleteById(couponId);
	}

	public List<DiscountDTO> getAllApplicableCoupons(Cart cart) {
		// TODO Auto-generated method stub
		List<DiscountDTO> discounts = new ArrayList<>();
		this.findCartDiscount(cart).ifPresent(discounts::add);
//		this.findProductDiscount(cart).ifPresent(discounts::addAll);
		return discounts;
	}

	private Optional<List<DiscountDTO>> findProductDiscount(Cart cart) {
		// TODO Auto-generated method stub
		List<DiscountDTO> prodCoupons = new ArrayList<>();
		for(CartItem item:cart.getItems()) {
			Product product = productRepo.findById(item.getProductId()).get();
			Coupon coupon = couponRepo.findFirstByProductAndExpiryDateAfter(product,LocalDateTime.now()).get();
			
		}
		return Optional.of(prodCoupons);
	}

	private Optional<DiscountDTO> findCartDiscount(Cart cart) {
		BigDecimal sum = cart.getItems().stream().map(cartItem -> productRepo.findById(cartItem.getProductId())
				.map(Product::getPrice).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);

		return couponRepo.findFirstByThresholdAndExpiryDateAfter(sum, LocalDateTime.now())
				.map(coupon -> DiscountDTO.builder().couponId(coupon.getId()).couponType(coupon.getCouponType())
						.discount(coupon.getThreshold()).build());
	}

//	@Transactional
//	public CouponDTO updateCoupon(CouponDTO coupondto) {
//		// TODO Auto-generated method stub
//		if(coupondto.getCouponId()==null) {
//			throw GenericApplicationException.builder().summary("Coupon not found").statusCode(HttpStatus.NOT_FOUND).build();
//		}
//		
//	}
}