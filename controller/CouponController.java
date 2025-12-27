package org.example.demomanagementsystemcproject.controller;

import org.example.demomanagementsystemcproject.dto.CouponDTO;
import org.example.demomanagementsystemcproject.dto.UserCouponDTO;
import org.example.demomanagementsystemcproject.service.CouponService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/marketing/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    // 优惠券管理接口

    @GetMapping
    public ResponseEntity<List<CouponDTO>> getAllCoupons() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<CouponDTO>> getCoupons(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(couponService.getCoupons(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponDTO> getCouponById(@PathVariable Long id) {
        return ResponseEntity.ok(couponService.getCouponById(id));
    }

    @PostMapping
    public ResponseEntity<CouponDTO> createCoupon(@RequestBody CouponDTO couponDTO) {
        return ResponseEntity.ok(couponService.createCoupon(couponDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CouponDTO> updateCoupon(@PathVariable Long id, @RequestBody CouponDTO couponDTO) {
        return ResponseEntity.ok(couponService.updateCoupon(id, couponDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateCouponStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        couponService.updateCouponStatus(id, body.get("status"));
        return ResponseEntity.ok().build();
    }

    // 用户优惠券接口

    @GetMapping("/available")
    public ResponseEntity<List<CouponDTO>> getAvailableCoupons(@RequestParam Long userId) {
        return ResponseEntity.ok(couponService.getAvailableCoupons(userId));
    }

    @PostMapping("/claim")
    public ResponseEntity<?> claimCoupon(@RequestBody Map<String, Long> body) {
        try {
            Long userId = body.get("userId");
            Long couponId = body.get("couponId");
            UserCouponDTO userCoupon = couponService.claimCoupon(userId, couponId);
            return ResponseEntity.ok(userCoupon);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserCouponDTO>> getUserCoupons(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "ALL") String status) {
        return ResponseEntity.ok(couponService.getUserCoupons(userId, status));
    }

    @GetMapping("/user/{userId}/page")
    public ResponseEntity<Page<UserCouponDTO>> getUserCouponsPage(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(couponService.getUserCouponsPage(userId, page, size));
    }

    @PostMapping("/use")
    public ResponseEntity<?> useCoupon(@RequestBody Map<String, Long> body) {
        try {
            Long userCouponId = body.get("userCouponId");
            Long orderId = body.get("orderId");
            couponService.useCoupon(userCouponId, orderId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/return/{userCouponId}")
    public ResponseEntity<?> returnCoupon(@PathVariable Long userCouponId) {
        try {
            couponService.returnCoupon(userCouponId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }

    // 工具接口

    @PostMapping("/check-expired")
    public ResponseEntity<Void> checkExpiredCoupons() {
        couponService.checkExpiredCoupons();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/expiring")
    public ResponseEntity<List<CouponDTO>> getExpiringCoupons(@RequestParam(defaultValue = "3") int days) {
        return ResponseEntity.ok(couponService.getExpiringCoupons(days));
    }
}