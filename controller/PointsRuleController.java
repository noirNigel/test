package org.example.demomanagementsystemcproject.controller;

import org.example.demomanagementsystemcproject.dto.PointsRuleDTO;
import org.example.demomanagementsystemcproject.service.PointsRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system/points-rule")
public class PointsRuleController {

    private final PointsRuleService pointsRuleService;

    public PointsRuleController(PointsRuleService pointsRuleService) {
        this.pointsRuleService = pointsRuleService;
    }

    @GetMapping
    public ResponseEntity<PointsRuleDTO> getRule() {
        return ResponseEntity.ok(pointsRuleService.getRule());
    }

    @PutMapping
    public ResponseEntity<PointsRuleDTO> updateRule(@RequestBody PointsRuleDTO dto) {
        return ResponseEntity.ok(pointsRuleService.updateRule(dto));
    }
}
