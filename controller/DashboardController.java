package org.example.demomanagementsystemcproject.controller;


import org.example.demomanagementsystemcproject.dto.*;
import org.example.demomanagementsystemcproject.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService svc;

    public DashboardController(DashboardService svc) {
        this.svc = svc;
    }

    /**
     * 仪表盘总览
     * GET /api/dashboard/summary
     */
    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> summary() {
        DashboardSummaryDTO dto = svc.getSummary();
        return ResponseEntity.ok(dto);
    }

    /**
     * 销售趋势（过去 n 天，默认7）
     * GET /api/dashboard/trend?days=7
     */
    @GetMapping("/trend")
    public ResponseEntity<List<DayCountDTO>> trend(@RequestParam(name = "days", defaultValue = "7") int days) {
        List<DayCountDTO> list = svc.getSalesTrend(days);
        return ResponseEntity.ok(list);
    }

    /**
     * 商品排行（过去 days 天，默认30，limit 默认10）
     * GET /api/dashboard/top-products?days=30&limit=10
     */
    @GetMapping("/top-products")
    public ResponseEntity<List<ProductRankDTO>> topProducts(@RequestParam(name = "days", defaultValue = "30") int days,
                                                            @RequestParam(name = "limit", defaultValue = "10") int limit) {
        List<ProductRankDTO> list = svc.getTopProducts(days, limit);
        return ResponseEntity.ok(list);
    }

    /**
     * 库存预警（阈值可选）
     * GET /api/dashboard/warnings/inventory?threshold=10
     */
    @GetMapping("/warnings/inventory")
    public ResponseEntity<List<WarningDTO>> inventoryWarnings(@RequestParam(name = "threshold", required = false) Integer threshold) {
        List<WarningDTO> list = svc.getInventoryWarnings(threshold);
        return ResponseEntity.ok(list);
    }
}
