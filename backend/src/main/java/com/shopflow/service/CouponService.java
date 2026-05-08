package com.shopflow.service;

import com.shopflow.dto.request.CouponRequest;
import com.shopflow.dto.response.CouponResponse;
import com.shopflow.entity.Coupon;
import com.shopflow.exception.ShopFlowException;
import com.shopflow.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Transactional(readOnly = true)
    public List<CouponResponse> getAllCoupons() {
        return couponRepository.findAll().stream()
                .map(this::toCouponResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CouponResponse createCoupon(CouponRequest request) {
        if (couponRepository.existsByCode(request.getCode())) {
            throw ShopFlowException.conflict("Un coupon avec ce code existe déjà");
        }

        Coupon coupon = Coupon.builder()
                .code(request.getCode().toUpperCase())
                .type(request.getType())
                .valeur(request.getValeur())
                .dateExpiration(request.getDateExpiration())
                .usagesMax(request.getUsagesMax() != null ? request.getUsagesMax() : 100)
                .usagesActuels(0)
                .actif(true)
                .build();

        coupon = couponRepository.save(coupon);
        return toCouponResponse(coupon);
    }

    @Transactional
    public CouponResponse updateCoupon(Long id, CouponRequest request) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> ShopFlowException.notFound("Coupon non trouvé : " + id));

        coupon.setCode(request.getCode().toUpperCase());
        coupon.setType(request.getType());
        coupon.setValeur(request.getValeur());
        coupon.setDateExpiration(request.getDateExpiration());
        if (request.getUsagesMax() != null) coupon.setUsagesMax(request.getUsagesMax());

        coupon = couponRepository.save(coupon);
        return toCouponResponse(coupon);
    }

    @Transactional
    public void deleteCoupon(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> ShopFlowException.notFound("Coupon non trouvé : " + id));
        coupon.setActif(false);
        couponRepository.save(coupon);
    }

    @Transactional(readOnly = true)
    public CouponResponse validateCoupon(String code) {
        Coupon coupon = couponRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> ShopFlowException.notFound("Code promo invalide"));

        if (!coupon.isActif()) {
            throw ShopFlowException.badRequest("Ce code promo n'est plus actif");
        }
        if (coupon.getDateExpiration() != null && coupon.getDateExpiration().isBefore(LocalDateTime.now())) {
            throw ShopFlowException.badRequest("Ce code promo a expiré");
        }
        if (coupon.getUsagesActuels() >= coupon.getUsagesMax()) {
            throw ShopFlowException.badRequest("Ce code promo a atteint son nombre maximum d'utilisations");
        }

        return toCouponResponse(coupon);
    }

    public CouponResponse toCouponResponse(Coupon coupon) {
        return CouponResponse.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .type(coupon.getType())
                .valeur(coupon.getValeur())
                .dateExpiration(coupon.getDateExpiration())
                .usagesMax(coupon.getUsagesMax())
                .usagesActuels(coupon.getUsagesActuels())
                .actif(coupon.isActif())
                .build();
    }
}
