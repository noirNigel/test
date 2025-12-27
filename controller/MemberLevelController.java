package org.example.demomanagementsystemcproject.controller;

import org.example.demomanagementsystemcproject.dto.MemberLevelDTO;
import org.example.demomanagementsystemcproject.service.MemberLevelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/marketing/member-levels")
public class MemberLevelController {

    private static final Logger logger = LoggerFactory.getLogger(MemberLevelController.class);

    private final MemberLevelService memberLevelService;

    public MemberLevelController(MemberLevelService memberLevelService) {
        this.memberLevelService = memberLevelService;
    }

    @GetMapping
    public ResponseEntity<List<MemberLevelDTO>> getAllLevels() {
        logger.info("开始查询所有会员等级");

        List<MemberLevelDTO> levels = memberLevelService.getAllLevels();
        logger.info("查询到会员等级数量: {}", levels.size());

        for (MemberLevelDTO level : levels) {
            logger.info("会员等级: ID={}, 名称={}, 状态={}",
                    level.getId(), level.getName(), level.getStatus());
        }

        return ResponseEntity.ok(levels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberLevelDTO> getLevelById(@PathVariable Long id) {
        return ResponseEntity.ok(memberLevelService.getLevelById(id));
    }

    @PostMapping
    public ResponseEntity<MemberLevelDTO> createLevel(@RequestBody MemberLevelDTO levelDTO) {
        return ResponseEntity.ok(memberLevelService.createLevel(levelDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberLevelDTO> updateLevel(@PathVariable Long id, @RequestBody MemberLevelDTO levelDTO) {
        return ResponseEntity.ok(memberLevelService.updateLevel(id, levelDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLevel(@PathVariable Long id) {
        memberLevelService.deleteLevel(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateLevelStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        memberLevelService.updateLevelStatus(id, body.get("status"));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user-level")
    public ResponseEntity<MemberLevelDTO> getUserLevel(@RequestParam Integer points) {
        MemberLevelDTO level = memberLevelService.getUserLevel(points);
        if (level == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(level);
    }

    @PostMapping("/calculate-price")
    public ResponseEntity<Map<String, Object>> calculateMemberPrice(@RequestBody Map<String, Object> body) {
        try {
            BigDecimal originalPrice = new BigDecimal(body.get("originalPrice").toString());
            Integer userPoints = (Integer) body.get("userPoints");

            BigDecimal memberPrice = memberLevelService.calculateMemberPrice(originalPrice, userPoints);

            return ResponseEntity.ok(Map.of(
                    "originalPrice", originalPrice,
                    "memberPrice", memberPrice,
                    "discount", originalPrice.subtract(memberPrice)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    @PostMapping("/calculate-points")
    public ResponseEntity<Map<String, Object>> calculateEarnedPoints(@RequestBody Map<String, Object> body) {
        try {
            BigDecimal orderAmount = new BigDecimal(body.get("orderAmount").toString());
            Integer userPoints = (Integer) body.get("userPoints");

            Integer earnedPoints = memberLevelService.calculateEarnedPoints(orderAmount, userPoints);

            return ResponseEntity.ok(Map.of(
                    "orderAmount", orderAmount,
                    "earnedPoints", earnedPoints
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }
}