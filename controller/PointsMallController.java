package org.example.demomanagementsystemcproject.controller;

import org.example.demomanagementsystemcproject.dto.PointsGoodsDTO;
import org.example.demomanagementsystemcproject.dto.PointsExchangeDTO;
import org.example.demomanagementsystemcproject.service.PointsMallService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/marketing/points-mall")
public class PointsMallController {

    private final PointsMallService pointsMallService;

    public PointsMallController(PointsMallService pointsMallService) {
        this.pointsMallService = pointsMallService;
    }

    // 积分商品管理接口

    @GetMapping("/goods")
    public ResponseEntity<List<PointsGoodsDTO>> getAllGoods() {
        return ResponseEntity.ok(pointsMallService.getAllGoods());
    }

    @GetMapping("/goods/page")
    public ResponseEntity<Page<PointsGoodsDTO>> getGoods(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(pointsMallService.getGoods(page, size));
    }

    @GetMapping("/goods/{id}")
    public ResponseEntity<PointsGoodsDTO> getGoodsById(@PathVariable Long id) {
        return ResponseEntity.ok(pointsMallService.getGoodsById(id));
    }

    @PostMapping("/goods")
    public ResponseEntity<PointsGoodsDTO> createGoods(@RequestBody PointsGoodsDTO goodsDTO) {
        return ResponseEntity.ok(pointsMallService.createGoods(goodsDTO));
    }

    @PutMapping("/goods/{id}")
    public ResponseEntity<PointsGoodsDTO> updateGoods(@PathVariable Long id, @RequestBody PointsGoodsDTO goodsDTO) {
        return ResponseEntity.ok(pointsMallService.updateGoods(id, goodsDTO));
    }

    @DeleteMapping("/goods/{id}")
    public ResponseEntity<Void> deleteGoods(@PathVariable Long id) {
        pointsMallService.deleteGoods(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/goods/{id}/status")
    public ResponseEntity<Void> updateGoodsStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        pointsMallService.updateGoodsStatus(id, body.get("status"));
        return ResponseEntity.ok().build();
    }

    // 积分兑换接口

    @GetMapping("/goods/available")
    public ResponseEntity<List<PointsGoodsDTO>> getAvailableGoods() {
        return ResponseEntity.ok(pointsMallService.getAvailableGoods());
    }

    @PostMapping("/exchange")
    public ResponseEntity<?> exchangeGoods(@RequestBody Map<String, Object> body) {
        try {
            Long userId = Long.valueOf(body.get("userId").toString());
            Long goodsId = Long.valueOf(body.get("goodsId").toString());
            Integer quantity = body.get("quantity") != null ? Integer.valueOf(body.get("quantity").toString()) : 1;
            String address = body.get("address") != null ? body.get("address").toString() : "";

            PointsExchangeDTO exchange = pointsMallService.exchangeGoods(userId, goodsId, quantity, address);
            return ResponseEntity.ok(exchange);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/exchange/user/{userId}")
    public ResponseEntity<List<PointsExchangeDTO>> getUserExchanges(@PathVariable Long userId) {
        return ResponseEntity.ok(pointsMallService.getUserExchanges(userId));
    }

    @GetMapping("/exchange/user/{userId}/page")
    public ResponseEntity<Page<PointsExchangeDTO>> getUserExchangesPage(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(pointsMallService.getUserExchangesPage(userId, page, size));
    }

    @PutMapping("/exchange/{exchangeId}/status")
    public ResponseEntity<Void> updateExchangeStatus(
            @PathVariable Long exchangeId,
            @RequestBody Map<String, String> body) {
        pointsMallService.updateExchangeStatus(exchangeId, body.get("status"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/exchange/check")
    public ResponseEntity<Map<String, Object>> checkCanExchange(@RequestBody Map<String, Object> body) {
        try {
            Long userId = Long.valueOf(body.get("userId").toString());
            Long goodsId = Long.valueOf(body.get("goodsId").toString());
            Integer quantity = body.get("quantity") != null ? Integer.valueOf(body.get("quantity").toString()) : 1;

            boolean canExchange = pointsMallService.canUserExchange(userId, goodsId, quantity);

            return ResponseEntity.ok(Map.of(
                    "canExchange", canExchange,
                    "userId", userId,
                    "goodsId", goodsId,
                    "quantity", quantity
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }
}