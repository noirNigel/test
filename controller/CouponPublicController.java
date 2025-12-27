package org.example.demomanagementsystemcproject.controller;

import org.example.demomanagementsystemcproject.dto.CouponDTO;
import org.example.demomanagementsystemcproject.dto.UserCouponDTO;
import org.example.demomanagementsystemcproject.service.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coupons")
public class CouponPublicController {

    private final CouponService couponService;

    public CouponPublicController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping("/available")
    public ResponseEntity<List<CouponDTO>> getAvailableCoupons(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestHeader(value = "X-User-Id", required = false) Long headerUserId
    ) {
        Long resolvedUserId = userId != null ? userId : headerUserId;
        if (resolvedUserId == null) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        return ResponseEntity.ok(couponService.getAvailableCoupons(resolvedUserId));
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyCoupons(
            @RequestParam(value = "status", defaultValue = "ALL") String status,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestHeader(value = "X-User-Id", required = false) Long headerUserId
    ) {
        Long resolvedUserId = userId != null ? userId : headerUserId;
        if (resolvedUserId == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", "userId is required"
            ));
        }
        List<UserCouponDTO> userCoupons = couponService.getUserCoupons(resolvedUserId, status);
        return ResponseEntity.ok(userCoupons);
    }
}
