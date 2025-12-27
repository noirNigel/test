package org.example.demomanagementsystemcproject.controller;

import org.example.demomanagementsystemcproject.dto.MarketingBannerDTO;
import org.example.demomanagementsystemcproject.service.MarketingBannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/marketing/banners")
public class MarketingBannerController {

    private static final Logger logger = LoggerFactory.getLogger(MarketingBannerController.class);

    private final MarketingBannerService marketingBannerService;

    public MarketingBannerController(MarketingBannerService marketingBannerService) {
        this.marketingBannerService = marketingBannerService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBanners() {
        List<MarketingBannerDTO> banners = marketingBannerService.getAllBanners();
        logger.info("查询到轮播图数量: {}", banners.size());
        return ResponseEntity.ok(Map.of("data", banners));
    }

    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>> getActiveBanners() {
        return ResponseEntity.ok(Map.of("data", marketingBannerService.getActiveBanners()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarketingBannerDTO> getBannerById(@PathVariable Long id) {
        return ResponseEntity.ok(marketingBannerService.getBannerById(id));
    }

    @PostMapping
    public ResponseEntity<MarketingBannerDTO> createBanner(@RequestBody MarketingBannerDTO bannerDTO) {
        return ResponseEntity.ok(marketingBannerService.createBanner(bannerDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarketingBannerDTO> updateBanner(@PathVariable Long id, @RequestBody MarketingBannerDTO bannerDTO) {
        return ResponseEntity.ok(marketingBannerService.updateBanner(id, bannerDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        marketingBannerService.deleteBanner(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateBannerStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        marketingBannerService.updateBannerStatus(id, body.get("status"));
        return ResponseEntity.ok().build();
    }
}
