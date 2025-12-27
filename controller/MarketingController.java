package org.example.demomanagementsystemcproject.controller;

import org.example.demomanagementsystemcproject.dto.CouponDTO;
import org.example.demomanagementsystemcproject.dto.PromotionDTO;
import org.example.demomanagementsystemcproject.dto.PointsGoodsDTO;
import org.example.demomanagementsystemcproject.service.CouponService;
import org.example.demomanagementsystemcproject.service.PromotionService;
import org.example.demomanagementsystemcproject.service.PointsMallService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/marketing")
public class MarketingController {

    private final CouponService couponService;
    private final PromotionService promotionService;
    private final PointsMallService pointsMallService;

    public MarketingController(CouponService couponService,
                               PromotionService promotionService,
                               PointsMallService pointsMallService) {
        this.couponService = couponService;
        this.promotionService = promotionService;
        this.pointsMallService = pointsMallService;
    }

    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getMarketingOverview() {
        Map<String, Object> overview = new HashMap<>();

        // 获取可用优惠券数量
        List<CouponDTO> availableCoupons = couponService.getAvailableCoupons(1L); // 使用示例用户ID
        overview.put("availableCoupons", availableCoupons.size());

        // 获取进行中的促销活动
        List<PromotionDTO> activePromotions = promotionService.getActivePromotions();
        overview.put("activePromotions", activePromotions.size());

        // 获取可兑换积分商品
        List<PointsGoodsDTO> availableGoods = pointsMallService.getAvailableGoods();
        overview.put("availableGoods", availableGoods.size());

        // 获取即将过期的优惠券
        List<CouponDTO> expiringCoupons = couponService.getExpiringCoupons(7); // 7天内过期
        overview.put("expiringCoupons", expiringCoupons.size());

        return ResponseEntity.ok(overview);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getMarketingDashboard() {
        Map<String, Object> dashboard = new HashMap<>();

        // 优惠券数据
        List<CouponDTO> coupons = couponService.getAllCoupons();
        dashboard.put("totalCoupons", coupons.size());
        dashboard.put("activeCoupons", coupons.stream().filter(c -> c.getStatus() == 1).count());

        // 促销活动数据
        List<PromotionDTO> promotions = promotionService.getAllPromotions();
        dashboard.put("totalPromotions", promotions.size());
        dashboard.put("activePromotions", promotions.stream().filter(p -> p.getStatus() == 1).count());

        // 积分商品数据
        List<PointsGoodsDTO> goods = pointsMallService.getAllGoods();
        dashboard.put("totalGoods", goods.size());
        dashboard.put("availableGoods", goods.stream().filter(g -> g.getStatus() == 1 && g.getStock() > 0).count());

        return ResponseEntity.ok(dashboard);
    }
}