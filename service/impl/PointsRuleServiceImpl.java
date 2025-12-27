package org.example.demomanagementsystemcproject.service.impl;

import org.example.demomanagementsystemcproject.dto.PointsRuleDTO;
import org.example.demomanagementsystemcproject.entity.PointsRuleEntity;
import org.example.demomanagementsystemcproject.repo.PointsRuleRepository;
import org.example.demomanagementsystemcproject.service.PointsRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PointsRuleServiceImpl implements PointsRuleService {

    private static final Logger log = LoggerFactory.getLogger(PointsRuleServiceImpl.class);

    private final PointsRuleRepository pointsRuleRepository;

    public PointsRuleServiceImpl(PointsRuleRepository pointsRuleRepository) {
        this.pointsRuleRepository = pointsRuleRepository;
    }

    @Override
    public PointsRuleDTO getActiveRule() {
        return pointsRuleRepository.findLatestActive()
                .map(this::toDto)
                .orElseGet(this::defaultRule);
    }

    @Override
    public PointsRuleDTO getRule() {
        return pointsRuleRepository.findAll().stream()
                .sorted((a, b) -> Long.compare(b.getId(), a.getId()))
                .findFirst()
                .map(this::toDto)
                .orElseGet(this::defaultRule);
    }

    @Override
    @Transactional
    public PointsRuleDTO updateRule(PointsRuleDTO dto) {
        PointsRuleEntity entity = pointsRuleRepository.findAll().stream()
                .sorted((a, b) -> Long.compare(b.getId(), a.getId()))
                .findFirst()
                .orElse(new PointsRuleEntity());

        if (dto.getEarnPerYuan() != null) {
            entity.setEarnPerYuan(dto.getEarnPerYuan());
        }
        if (dto.getRedeemPoints() != null) {
            entity.setRedeemPoints(dto.getRedeemPoints());
        }
        if (dto.getRedeemYuan() != null) {
            entity.setRedeemYuan(dto.getRedeemYuan());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }

        PointsRuleEntity saved = pointsRuleRepository.save(entity);
        log.info("保存积分规则，earnPerYuan={}, redeemPoints={}, redeemYuan={}, status={}",
                saved.getEarnPerYuan(), saved.getRedeemPoints(), saved.getRedeemYuan(), saved.getStatus());
        return toDto(saved);
    }

    private PointsRuleDTO toDto(PointsRuleEntity entity) {
        PointsRuleDTO dto = new PointsRuleDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    private PointsRuleDTO defaultRule() {
        PointsRuleDTO dto = new PointsRuleDTO();
        dto.setEarnPerYuan(10);
        dto.setRedeemPoints(100);
        dto.setRedeemYuan(BigDecimal.ONE);
        dto.setStatus(1);
        return dto;
    }
}
