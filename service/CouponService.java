package org.example.demomanagementsystemcproject.service;

import org.example.demomanagementsystemcproject.dto.CouponDTO;
import org.example.demomanagementsystemcproject.dto.UserCouponDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface CouponService {

    // 优惠券管理
    List<CouponDTO> getAllCoupons();
    Page<CouponDTO> getCoupons(int page, int size);
    CouponDTO getCouponById(Long id);
    CouponDTO createCoupon(CouponDTO couponDTO);
    CouponDTO updateCoupon(Long id, CouponDTO couponDTO);
    void deleteCoupon(Long id);
    void updateCouponStatus(Long id, Integer status);

    // 用户优惠券
    List<CouponDTO> getAvailableCoupons(Long userId);
    UserCouponDTO claimCoupon(Long userId, Long couponId);
    List<UserCouponDTO> getUserCoupons(Long userId, String status);
    Page<UserCouponDTO> getUserCouponsPage(Long userId, int page, int size);
    void useCoupon(Long userCouponId, Long orderId);
    void returnCoupon(Long userCouponId);

    // 工具方法
    void checkExpiredCoupons();
    List<CouponDTO> getExpiringCoupons(int days);
}