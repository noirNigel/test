package org.example.demomanagementsystemcproject.service.impl;

import org.example.demomanagementsystemcproject.dto.PointsGoodsDTO;
import org.example.demomanagementsystemcproject.dto.PointsExchangeDTO;
import org.example.demomanagementsystemcproject.entity.Admin;
import org.example.demomanagementsystemcproject.entity.PointsGoodsEntity;
import org.example.demomanagementsystemcproject.entity.PointsExchangeEntity;
import org.example.demomanagementsystemcproject.repo.PointsGoodsRepository;
import org.example.demomanagementsystemcproject.repo.PointsExchangeRepository;
import org.example.demomanagementsystemcproject.repository.AdminRepository;
import org.example.demomanagementsystemcproject.service.PointsMallService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PointsMallServiceImpl implements PointsMallService {

    private final PointsGoodsRepository pointsGoodsRepository;
    private final PointsExchangeRepository pointsExchangeRepository;
    private final AdminRepository adminRepository;

    public PointsMallServiceImpl(PointsGoodsRepository pointsGoodsRepository,
                                 PointsExchangeRepository pointsExchangeRepository,
                                 AdminRepository adminRepository) {
        this.pointsGoodsRepository = pointsGoodsRepository;
        this.pointsExchangeRepository = pointsExchangeRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public List<PointsGoodsDTO> getAllGoods() {
        return pointsGoodsRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<PointsGoodsDTO> getGoods(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return pointsGoodsRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Override
    public PointsGoodsDTO getGoodsById(Long id) {
        PointsGoodsEntity entity = pointsGoodsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("积分商品不存在"));
        return convertToDTO(entity);
    }

    @Override
    @Transactional
    public PointsGoodsDTO createGoods(PointsGoodsDTO goodsDTO) {
        validateGoods(goodsDTO);

        PointsGoodsEntity entity = new PointsGoodsEntity();
        BeanUtils.copyProperties(goodsDTO, entity);

        // 设置总库存
        if (entity.getStock() != null) {
            entity.setTotalStock(entity.getStock());
        }

        PointsGoodsEntity saved = pointsGoodsRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public PointsGoodsDTO updateGoods(Long id, PointsGoodsDTO goodsDTO) {
        PointsGoodsEntity entity = pointsGoodsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("积分商品不存在"));

        validateGoods(goodsDTO);

        BeanUtils.copyProperties(goodsDTO, entity, "id", "totalStock");
        PointsGoodsEntity saved = pointsGoodsRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public void deleteGoods(Long id) {
        if (!pointsGoodsRepository.existsById(id)) {
            throw new RuntimeException("积分商品不存在");
        }
        pointsGoodsRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateGoodsStatus(Long id, Integer status) {
        PointsGoodsEntity entity = pointsGoodsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("积分商品不存在"));
        entity.setStatus(status);
        pointsGoodsRepository.save(entity);
    }

    @Override
    public List<PointsGoodsDTO> getAvailableGoods() {
        return pointsGoodsRepository.findAvailableGoods().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PointsExchangeDTO exchangeGoods(Long userId, Long goodsId, Integer quantity, String address) {
        // 检查商品是否存在且可兑换
        PointsGoodsEntity goods = pointsGoodsRepository.findById(goodsId)
                .orElseThrow(() -> new RuntimeException("积分商品不存在"));

        if (goods.getStatus() != 1) {
            throw new RuntimeException("商品已下架");
        }

        if (goods.getStock() < quantity) {
            throw new RuntimeException("商品库存不足");
        }

        // 检查用户是否可以兑换（这里需要集成用户积分系统）
        // 假设有一个方法可以获取用户积分
        Integer userPoints = getUserPoints(userId);
        Integer requiredPoints = goods.getPointsRequired() * quantity;

        if (userPoints < requiredPoints) {
            throw new RuntimeException("积分不足");
        }

        // 检查今日兑换次数限制
        Long todayExchanges = pointsExchangeRepository.countTodayExchangesByUserId(userId);
        if (todayExchanges >= 10) { // 每日限制10次
            throw new RuntimeException("今日兑换次数已达上限");
        }

        // 创建兑换记录
        PointsExchangeEntity exchange = new PointsExchangeEntity();
        exchange.setUserId(userId);
        exchange.setGoodsId(goodsId);
        exchange.setGoodsName(goods.getName());
        exchange.setPointsUsed(requiredPoints);
        exchange.setQuantity(quantity);
        exchange.setAddress(address);

        PointsExchangeEntity saved = pointsExchangeRepository.save(exchange);

        // 更新商品库存
        goods.setStock(goods.getStock() - quantity);
        pointsGoodsRepository.save(goods);

        // 扣除用户积分（这里需要调用用户服务）
        deductUserPoints(userId, requiredPoints);

        return convertToExchangeDTO(saved);
    }

    @Override
    public List<PointsExchangeDTO> getUserExchanges(Long userId) {
        return pointsExchangeRepository.findByUserIdOrderByExchangeTimeDesc(userId).stream()
                .map(this::convertToExchangeDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<PointsExchangeDTO> getUserExchangesPage(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return pointsExchangeRepository.findByUserId(userId, pageable)
                .map(this::convertToExchangeDTO);
    }

    @Override
    @Transactional
    public void updateExchangeStatus(Long exchangeId, String status) {
        PointsExchangeEntity exchange = pointsExchangeRepository.findById(exchangeId)
                .orElseThrow(() -> new RuntimeException("兑换记录不存在"));

        if ("COMPLETED".equals(status)) {
            exchange.setCompleteTime(LocalDateTime.now());
        }

        exchange.setStatus(status);
        pointsExchangeRepository.save(exchange);
    }

    @Override
    public boolean canUserExchange(Long userId, Long goodsId, Integer quantity) {
        try {
            PointsGoodsEntity goods = pointsGoodsRepository.findById(goodsId)
                    .orElseThrow(() -> new RuntimeException("积分商品不存在"));

            if (goods.getStatus() != 1 || goods.getStock() < quantity) {
                return false;
            }

            Integer userPoints = getUserPoints(userId);
            Integer requiredPoints = goods.getPointsRequired() * quantity;

            return userPoints >= requiredPoints;
        } catch (Exception e) {
            return false;
        }
    }

    private void validateGoods(PointsGoodsDTO goodsDTO) {
        if (goodsDTO.getName() == null || goodsDTO.getName().trim().isEmpty()) {
            throw new RuntimeException("商品名称不能为空");
        }

        if (goodsDTO.getPointsRequired() == null || goodsDTO.getPointsRequired() <= 0) {
            throw new RuntimeException("所需积分必须大于0");
        }

        if (goodsDTO.getStock() == null || goodsDTO.getStock() < 0) {
            throw new RuntimeException("库存不能为负数");
        }
    }

    private PointsGoodsDTO convertToDTO(PointsGoodsEntity entity) {
        PointsGoodsDTO dto = new PointsGoodsDTO();
        BeanUtils.copyProperties(entity, dto);

        if (entity.getStatus() != null) {
            dto.setStatusText(entity.getStatus() == 1 ? "上架" : "下架");
        }

        // 检查是否可以兑换（这里需要集成用户积分系统）
        dto.setCanExchange(entity.getStatus() == 1 && entity.getStock() > 0);

        return dto;
    }

    private PointsExchangeDTO convertToExchangeDTO(PointsExchangeEntity entity) {
        PointsExchangeDTO dto = new PointsExchangeDTO();
        BeanUtils.copyProperties(entity, dto);

        // 设置状态文本
        switch (entity.getStatus()) {
            case "PENDING":
                dto.setStatusText("待处理");
                break;
            case "COMPLETED":
                dto.setStatusText("已完成");
                break;
            case "CANCELLED":
                dto.setStatusText("已取消");
                break;
            default:
                dto.setStatusText("未知");
        }

        // 加载商品信息
        PointsGoodsEntity goods = pointsGoodsRepository.findById(entity.getGoodsId()).orElse(null);
        if (goods != null) {
            dto.setGoods(convertToDTO(goods));
        }

        return dto;
    }

    private Integer getUserPoints(Long userId) {
        Admin admin = adminRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return admin.getAvailablePoints() == null ? 0 : admin.getAvailablePoints();
    }

    private void deductUserPoints(Long userId, Integer points) {
        Admin admin = adminRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        Integer current = admin.getAvailablePoints() == null ? 0 : admin.getAvailablePoints();
        int next = current - points;
        admin.setAvailablePoints(Math.max(next, 0));
        adminRepository.save(admin);
    }
}