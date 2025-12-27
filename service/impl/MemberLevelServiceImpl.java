package org.example.demomanagementsystemcproject.service.impl;

import org.example.demomanagementsystemcproject.dto.MemberLevelDTO;
import org.example.demomanagementsystemcproject.entity.MemberLevelEntity;
import org.example.demomanagementsystemcproject.repo.MemberLevelRepository;
import org.example.demomanagementsystemcproject.service.MemberLevelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberLevelServiceImpl implements MemberLevelService {

    private static final Logger logger = LoggerFactory.getLogger(MemberLevelServiceImpl.class);

    private final MemberLevelRepository memberLevelRepository;

    public MemberLevelServiceImpl(MemberLevelRepository memberLevelRepository) {
        this.memberLevelRepository = memberLevelRepository;
    }

    @Override
    public List<MemberLevelDTO> getAllLevels() {
        logger.info("开始查询所有会员等级");

        List<MemberLevelEntity> entities = memberLevelRepository.findAll(Sort.by(Sort.Direction.ASC, "minPoints"));

        logger.info("查询到会员等级数量: {}", entities.size());

        List<MemberLevelDTO> result = entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        logger.info("转换后返回 {} 条DTO", result.size());
        return result;
    }

    @Override
    public MemberLevelDTO getLevelById(Long id) {
        MemberLevelEntity entity = memberLevelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("会员等级不存在"));
        return convertToDTO(entity);
    }

    @Override
    @Transactional
    public MemberLevelDTO createLevel(MemberLevelDTO levelDTO) {
        logger.info("开始创建会员等级: {}", levelDTO.getName());

        validateLevel(levelDTO);

        MemberLevelEntity entity = new MemberLevelEntity();
        BeanUtils.copyProperties(levelDTO, entity);

        // 确保状态不为空，默认为启用状态
        if (entity.getStatus() == null) {
            entity.setStatus(1);
            logger.info("设置默认状态为: 1");
        }

        MemberLevelEntity saved = memberLevelRepository.save(entity);
        logger.info("会员等级创建成功, ID: {}", saved.getId());

        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public MemberLevelDTO updateLevel(Long id, MemberLevelDTO levelDTO) {
        MemberLevelEntity entity = memberLevelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("会员等级不存在"));

        validateLevel(levelDTO);

        BeanUtils.copyProperties(levelDTO, entity, "id");
        MemberLevelEntity saved = memberLevelRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public void deleteLevel(Long id) {
        if (!memberLevelRepository.existsById(id)) {
            throw new RuntimeException("会员等级不存在");
        }
        memberLevelRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateLevelStatus(Long id, Integer status) {
        MemberLevelEntity entity = memberLevelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("会员等级不存在"));
        entity.setStatus(status);
        memberLevelRepository.save(entity);
    }

    @Override
    public MemberLevelDTO getUserLevel(Integer points) {
        List<MemberLevelEntity> levels = memberLevelRepository.findActiveLevels();
        return resolveLevel(points, levels);
    }

    @Override
    public BigDecimal calculateMemberPrice(BigDecimal originalPrice, Integer userPoints) {
        MemberLevelDTO level = getUserLevel(userPoints);
        if (level == null || level.getDiscountRate() == null) {
            return originalPrice;
        }

        return originalPrice.multiply(level.getDiscountRate());
    }

    @Override
    public Integer calculateEarnedPoints(BigDecimal orderAmount, Integer userPoints) {
        MemberLevelDTO level = getUserLevel(userPoints);
        if (level == null || level.getPointsMultiplier() == null) {
            return orderAmount.intValue();
        }

        BigDecimal points = orderAmount.multiply(level.getPointsMultiplier());
        return points.intValue();
    }

    @Override
    public List<MemberLevelDTO> getActiveLevels() {
        return memberLevelRepository.findActiveLevels().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private MemberLevelDTO resolveLevel(Integer points, List<MemberLevelEntity> levels) {
        return levels.stream()
                .filter(level -> level.getMinPoints() != null && level.getMinPoints() <= points)
                .max((a, b) -> Integer.compare(
                        a.getMinPoints() == null ? 0 : a.getMinPoints(),
                        b.getMinPoints() == null ? 0 : b.getMinPoints()))
                .map(this::convertToDTO)
                .orElse(null);
    }

    private void validateLevel(MemberLevelDTO levelDTO) {
        if (levelDTO.getName() == null || levelDTO.getName().trim().isEmpty()) {
            throw new RuntimeException("等级名称不能为空");
        }

        if (levelDTO.getMinPoints() == null || levelDTO.getMinPoints() < 0) {
            throw new RuntimeException("最低积分不能为负数");
        }

        if (levelDTO.getDiscountRate() == null ||
                levelDTO.getDiscountRate().compareTo(BigDecimal.ZERO) <= 0 ||
                levelDTO.getDiscountRate().compareTo(BigDecimal.ONE) > 0) {
            throw new RuntimeException("折扣率必须在0-1之间");
        }

        if (levelDTO.getPointsMultiplier() == null ||
                levelDTO.getPointsMultiplier().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("积分倍数必须大于0");
        }
    }

    private MemberLevelDTO convertToDTO(MemberLevelEntity entity) {
        MemberLevelDTO dto = new MemberLevelDTO();
        BeanUtils.copyProperties(entity, dto);

        // 处理状态显示
        Integer status = entity.getStatus();
        if (status != null) {
            dto.setStatusText(status == 1 ? "启用" : "禁用");
        } else {
            dto.setStatusText("未知"); // 处理 null 状态
        }

        return dto;
    }
}