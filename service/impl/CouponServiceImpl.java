package org.example.demomanagementsystemcproject.service.impl;

import org.example.demomanagementsystemcproject.dto.CouponDTO;
import org.example.demomanagementsystemcproject.dto.UserCouponDTO;
import org.example.demomanagementsystemcproject.entity.CouponEntity;
import org.example.demomanagementsystemcproject.entity.UserCouponEntity;
import org.example.demomanagementsystemcproject.repo.CouponRepository;
import org.example.demomanagementsystemcproject.repo.UserCouponRepository;
import org.example.demomanagementsystemcproject.service.CouponService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    public CouponServiceImpl(CouponRepository couponRepository, UserCouponRepository userCouponRepository) {
        this.couponRepository = couponRepository;
        this.userCouponRepository = userCouponRepository;
    }

    @Override
    public List<CouponDTO> getAllCoupons() {
        return couponRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CouponDTO> getCoupons(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return couponRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Override
    public CouponDTO getCouponById(Long id) {
        CouponEntity entity = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("优惠券不存在"));
        return convertToDTO(entity);
    }

    @Override
    @Transactional
    public CouponDTO createCoupon(CouponDTO couponDTO) {
        // 验证数据
        validateCoupon(couponDTO);

        CouponEntity entity = new CouponEntity();
        BeanUtils.copyProperties(couponDTO, entity);

        // 设置剩余数量
        if (entity.getTotalCount() != null) {
            entity.setRemainingCount(entity.getTotalCount());
        }

        CouponEntity saved = couponRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public CouponDTO updateCoupon(Long id, CouponDTO couponDTO) {
        CouponEntity entity = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("优惠券不存在"));

        // 验证数据
        validateCoupon(couponDTO);

        BeanUtils.copyProperties(couponDTO, entity, "id", "remainingCount");
        CouponEntity saved = couponRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public void deleteCoupon(Long id) {
        if (!couponRepository.existsById(id)) {
            throw new RuntimeException("优惠券不存在");
        }
        couponRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateCouponStatus(Long id, Integer status) {
        CouponEntity entity = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("优惠券不存在"));
        entity.setStatus(status);
        couponRepository.save(entity);
    }

    @Override
    public List<CouponDTO> getAvailableCoupons(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        List<CouponEntity> availableCoupons = couponRepository.findAvailableCoupons(now);

        return availableCoupons.stream().map(coupon -> {
            CouponDTO dto = convertToDTO(coupon);

            // 检查用户是否可领取
            Long claimedCount = userCouponRepository.countByUserIdAndCouponId(userId, coupon.getId());
            dto.setUserClaimedCount(claimedCount.intValue());
            dto.setCanClaim(claimedCount < coupon.getLimitPerUser());

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserCouponDTO claimCoupon(Long userId, Long couponId) {
        LocalDateTime now = LocalDateTime.now();

        // 检查优惠券是否存在且可领取
        CouponEntity coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new RuntimeException("优惠券不存在"));

        if (coupon.getStatus() != 1) {
            throw new RuntimeException("优惠券不可用");
        }

        if (now.isBefore(coupon.getStartTime()) || now.isAfter(coupon.getEndTime())) {
            throw new RuntimeException("不在优惠券有效期内");
        }

        if (coupon.getRemainingCount() <= 0) {
            throw new RuntimeException("优惠券已领完");
        }

        // 检查用户领取数量
        Long claimedCount = userCouponRepository.countByUserIdAndCouponId(userId, couponId);
        if (claimedCount >= coupon.getLimitPerUser()) {
            throw new RuntimeException("已达到领取上限");
        }

        // 创建用户优惠券
        UserCouponEntity userCoupon = new UserCouponEntity();
        userCoupon.setUserId(userId);
        userCoupon.setCouponId(couponId);
        userCoupon.setExpiredTime(coupon.getEndTime());

        UserCouponEntity saved = userCouponRepository.save(userCoupon);

        // 更新优惠券剩余数量
        coupon.setRemainingCount(coupon.getRemainingCount() - 1);
        couponRepository.save(coupon);

        return convertToUserCouponDTO(saved);
    }

    @Override
    public List<UserCouponDTO> getUserCoupons(Long userId, String status) {
        List<UserCouponEntity> userCoupons;
        if ("ALL".equals(status)) {
            // 修复：保持查询方法不变，使用 Pageable.unpaged()
            userCoupons = userCouponRepository.findByUserId(userId, Pageable.unpaged()).getContent();
        } else {
            userCoupons = userCouponRepository.findByUserIdAndStatus(userId, status);
        }

        return userCoupons.stream()
                .map(this::convertToUserCouponDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserCouponDTO> getUserCouponsPage(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return userCouponRepository.findByUserId(userId, pageable)
                .map(this::convertToUserCouponDTO);
    }

    @Override
    @Transactional
    public void useCoupon(Long userCouponId, Long orderId) {
        UserCouponEntity userCoupon = userCouponRepository.findById(userCouponId)
                .orElseThrow(() -> new RuntimeException("用户优惠券不存在"));

        if (!"UNUSED".equals(userCoupon.getStatus())) {
            throw new RuntimeException("优惠券状态不可用");
        }

        if (LocalDateTime.now().isAfter(userCoupon.getExpiredTime())) {
            throw new RuntimeException("优惠券已过期");
        }

        userCoupon.setStatus("USED");
        userCoupon.setUseTime(LocalDateTime.now());
        userCoupon.setOrderId(orderId);
        userCouponRepository.save(userCoupon);
    }

    @Override
    @Transactional
    public void returnCoupon(Long userCouponId) {
        UserCouponEntity userCoupon = userCouponRepository.findById(userCouponId)
                .orElseThrow(() -> new RuntimeException("用户优惠券不存在"));

        if (!"USED".equals(userCoupon.getStatus())) {
            throw new RuntimeException("只有已使用的优惠券才能退回");
        }

        userCoupon.setStatus("UNUSED");
        userCoupon.setUseTime(null);
        userCoupon.setOrderId(null);
        userCouponRepository.save(userCoupon);
    }

    @Override
    @Transactional
    public void checkExpiredCoupons() {
        LocalDateTime now = LocalDateTime.now();
        List<UserCouponEntity> expiredCoupons = userCouponRepository.findExpiredUserCoupons(now);

        for (UserCouponEntity coupon : expiredCoupons) {
            if ("UNUSED".equals(coupon.getStatus())) {
                coupon.setStatus("EXPIRED");
                userCouponRepository.save(coupon);
            }
        }
    }

    @Override
    public List<CouponDTO> getExpiringCoupons(int days) {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(days);
        return couponRepository.findExpiringCoupons(start, end).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private void validateCoupon(CouponDTO couponDTO) {
        if (couponDTO.getName() == null || couponDTO.getName().trim().isEmpty()) {
            throw new RuntimeException("优惠券名称不能为空");
        }

        if (couponDTO.getType() == null) {
            throw new RuntimeException("优惠券类型不能为空");
        }

        if (couponDTO.getStartTime() == null || couponDTO.getEndTime() == null) {
            throw new RuntimeException("优惠券时间不能为空");
        }

        if (couponDTO.getEndTime().isBefore(couponDTO.getStartTime())) {
            throw new RuntimeException("结束时间不能早于开始时间");
        }

        if ("DISCOUNT".equals(couponDTO.getType())) {
            if (couponDTO.getDiscount() == null || couponDTO.getDiscount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("折扣优惠券必须设置折扣比例");
            }
        } else if ("REDUCE".equals(couponDTO.getType())) {
            if (couponDTO.getReduceAmount() == null || couponDTO.getReduceAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("满减优惠券必须设置减免金额");
            }
            if (couponDTO.getMinAmount() == null || couponDTO.getMinAmount().compareTo(couponDTO.getReduceAmount()) <= 0) {
                throw new RuntimeException("满减金额必须大于减免金额");
            }
        }
    }

    private CouponDTO convertToDTO(CouponEntity entity) {
        CouponDTO dto = new CouponDTO();
        BeanUtils.copyProperties(entity, dto);

        // 设置状态文本
        if (entity.getStatus() != null) {
            dto.setStatusText(entity.getStatus() == 1 ? "启用" : "禁用");
        }

        return dto;
    }

    private UserCouponDTO convertToUserCouponDTO(UserCouponEntity entity) {
        UserCouponDTO dto = new UserCouponDTO();
        BeanUtils.copyProperties(entity, dto);

        // 设置状态文本
        switch (entity.getStatus()) {
            case "UNUSED":
                dto.setStatusText("未使用");
                break;
            case "USED":
                dto.setStatusText("已使用");
                break;
            case "EXPIRED":
                dto.setStatusText("已过期");
                break;
            default:
                dto.setStatusText("未知");
        }

        // 检查是否过期
        if ("UNUSED".equals(entity.getStatus()) && LocalDateTime.now().isAfter(entity.getExpiredTime())) {
            dto.setIsExpired(true);
        } else {
            dto.setIsExpired(false);
        }

        // 加载优惠券信息
        CouponEntity coupon = couponRepository.findById(entity.getCouponId()).orElse(null);
        if (coupon != null) {
            dto.setCoupon(convertToDTO(coupon));
        }

        return dto;
    }
}