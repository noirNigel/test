package org.example.demomanagementsystemcproject.service.impl;

import org.example.demomanagementsystemcproject.dto.MemberLevelDTO;
import org.example.demomanagementsystemcproject.dto.MemberProfileDTO;
import org.example.demomanagementsystemcproject.dto.PointsRuleDTO;
import org.example.demomanagementsystemcproject.entity.Admin;
import org.example.demomanagementsystemcproject.repository.AdminRepository;
import org.example.demomanagementsystemcproject.service.MemberLevelService;
import org.example.demomanagementsystemcproject.service.MemberService;
import org.example.demomanagementsystemcproject.service.PointsRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    private static final Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);

    private final AdminRepository adminRepository;
    private final MemberLevelService memberLevelService;
    private final PointsRuleService pointsRuleService;

    public MemberServiceImpl(AdminRepository adminRepository,
                             MemberLevelService memberLevelService,
                             PointsRuleService pointsRuleService) {
        this.adminRepository = adminRepository;
        this.memberLevelService = memberLevelService;
        this.pointsRuleService = pointsRuleService;
    }

    @Override
    public MemberProfileDTO getMemberProfile(Long userId) {
        Admin user = adminRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        Integer levelPoints = Optional.ofNullable(user.getLevelPoints())
                .orElse(Optional.ofNullable(user.getPoints()).orElse(0));
        Integer availablePoints = Optional.ofNullable(user.getAvailablePoints())
                .orElse(Optional.ofNullable(user.getPoints()).orElse(0));
        PointsRuleDTO rule = pointsRuleService.getActiveRule();
        List<MemberLevelDTO> activeLevels = memberLevelService.getActiveLevels();

        MemberLevelDTO currentLevel = resolveCurrentLevel(levelPoints, activeLevels);
        MemberLevelDTO nextLevel = resolveNextLevel(levelPoints, activeLevels);

        MemberProfileDTO.UserSnapshot snapshot = new MemberProfileDTO.UserSnapshot();
        snapshot.setId(user.getId());
        snapshot.setPoints(availablePoints);
        snapshot.setLevelPoints(levelPoints);
        snapshot.setAvailablePoints(availablePoints);
        snapshot.setBalance(BigDecimal.ZERO);
        snapshot.setLevel(currentLevel != null ? currentLevel.getName() : "普通");
        snapshot.setLevelName(snapshot.getLevel());
        snapshot.setLevelMinPoints(currentLevel != null ? currentLevel.getMinPoints() : 0);
        snapshot.setNextLevel(nextLevel != null ? nextLevel.getName() : null);
        snapshot.setNextLevelMinPoints(nextLevel != null ? nextLevel.getMinPoints() : null);

        MemberProfileDTO response = new MemberProfileDTO();
        response.setUser(snapshot);
        response.setRules(rule);
        response.setLevels(activeLevels);

        log.info("生成会员信息: userId={}, levelPoints={}, availablePoints={}, level={}, nextLevel={}",
                userId, levelPoints, availablePoints, snapshot.getLevel(), snapshot.getNextLevel());
        return response;
    }

    private MemberLevelDTO resolveCurrentLevel(Integer points, List<MemberLevelDTO> levels) {
        return levels.stream()
                .filter(l -> l.getMinPoints() != null && l.getMinPoints() <= points)
                .max(Comparator.comparingInt(l -> Optional.ofNullable(l.getMinPoints()).orElse(0)))
                .orElse(null);
    }

    private MemberLevelDTO resolveNextLevel(Integer points, List<MemberLevelDTO> levels) {
        return levels.stream()
                .filter(l -> l.getMinPoints() != null && l.getMinPoints() > points)
                .min(Comparator.comparingInt(l -> Optional.ofNullable(l.getMinPoints()).orElse(Integer.MAX_VALUE)))
                .orElse(null);
    }
}
