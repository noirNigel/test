package org.example.demomanagementsystemcproject.service.impl;

import org.example.demomanagementsystemcproject.dto.MarketingBannerDTO;
import org.example.demomanagementsystemcproject.entity.MarketingBannerEntity;
import org.example.demomanagementsystemcproject.repo.MarketingBannerRepository;
import org.example.demomanagementsystemcproject.service.MarketingBannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarketingBannerServiceImpl implements MarketingBannerService {

    private static final Logger logger = LoggerFactory.getLogger(MarketingBannerServiceImpl.class);

    private final MarketingBannerRepository marketingBannerRepository;

    public MarketingBannerServiceImpl(MarketingBannerRepository marketingBannerRepository) {
        this.marketingBannerRepository = marketingBannerRepository;
    }

    @Override
    public List<MarketingBannerDTO> getAllBanners() {
        return marketingBannerRepository.findAllByOrderBySortOrderAscCreatedAtDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MarketingBannerDTO> getActiveBanners() {
        LocalDateTime now = LocalDateTime.now();
        return marketingBannerRepository.findActiveBanners(now)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MarketingBannerDTO getBannerById(Long id) {
        MarketingBannerEntity entity = marketingBannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("轮播图不存在"));
        return convertToDTO(entity);
    }

    @Override
    @Transactional
    public MarketingBannerDTO createBanner(MarketingBannerDTO bannerDTO) {
        validateBanner(bannerDTO);
        MarketingBannerEntity entity = new MarketingBannerEntity();
        BeanUtils.copyProperties(bannerDTO, entity);

        if (entity.getStatus() == null) {
            entity.setStatus(1);
        }

        MarketingBannerEntity saved = marketingBannerRepository.save(entity);
        logger.info("创建轮播图，ID={}, 标题={}", saved.getId(), saved.getTitle());
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public MarketingBannerDTO updateBanner(Long id, MarketingBannerDTO bannerDTO) {
        MarketingBannerEntity entity = marketingBannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("轮播图不存在"));

        validateBanner(bannerDTO);
        BeanUtils.copyProperties(bannerDTO, entity, "id", "createdAt", "updatedAt");

        MarketingBannerEntity saved = marketingBannerRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public void deleteBanner(Long id) {
        if (!marketingBannerRepository.existsById(id)) {
            throw new RuntimeException("轮播图不存在");
        }
        marketingBannerRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateBannerStatus(Long id, Integer status) {
        MarketingBannerEntity entity = marketingBannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("轮播图不存在"));
        entity.setStatus(status);
        marketingBannerRepository.save(entity);
    }

    private void validateBanner(MarketingBannerDTO bannerDTO) {
        if (bannerDTO.getTitle() == null || bannerDTO.getTitle().trim().isEmpty()) {
            throw new RuntimeException("标题不能为空");
        }
        if (bannerDTO.getImageUrl() == null || bannerDTO.getImageUrl().trim().isEmpty()) {
            throw new RuntimeException("图片地址不能为空");
        }
        if (bannerDTO.getStartTime() != null && bannerDTO.getEndTime() != null
                && bannerDTO.getEndTime().isBefore(bannerDTO.getStartTime())) {
            throw new RuntimeException("结束时间不能早于开始时间");
        }
    }

    private MarketingBannerDTO convertToDTO(MarketingBannerEntity entity) {
        MarketingBannerDTO dto = new MarketingBannerDTO();
        BeanUtils.copyProperties(entity, dto);

        dto.setImageUrl(normalizeImageSource(entity.getImageUrl()));

        LocalDateTime now = LocalDateTime.now();
        boolean activeStatus = (entity.getStatus() == null || entity.getStatus() == 1)
                && (entity.getStartTime() == null || !now.isBefore(entity.getStartTime()))
                && (entity.getEndTime() == null || !now.isAfter(entity.getEndTime()));
        dto.setIsActive(activeStatus);

        return dto;
    }

    /**
     * 后台存储的是本地上传图片的 base64 数据。如果前端/小程序没有传入 data:image/** 前缀，
     * 这里补全成可直接展示的 data URI，便于 <img> 和小程序 <image> 标签加载。
     */
    private String normalizeImageSource(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return raw;
        }
        String value = raw.trim();

        // 已经是完整的可访问地址或 data URI，直接返回
        if (value.startsWith("http://") || value.startsWith("https://") || value.startsWith("data:")) {
            return value;
        }

        // 如果缺少 data URI 头，则补全一个通用的 PNG 前缀，确保前端能够展示
        return "data:image/png;base64," + value;
    }
}
