package org.example.demomanagementsystemcproject.service.impl;

import org.example.demomanagementsystemcproject.dto.*;
import org.example.demomanagementsystemcproject.entity.Admin;
import org.example.demomanagementsystemcproject.entity.OrderEntity;
import org.example.demomanagementsystemcproject.entity.OrderItemEntity;
import org.example.demomanagementsystemcproject.entity.ProductEntity;
import org.example.demomanagementsystemcproject.repo.OrderRepository;
import org.example.demomanagementsystemcproject.repo.OrderItemRepository;
import org.example.demomanagementsystemcproject.repo.ProductRepository;
import org.example.demomanagementsystemcproject.repository.AdminRepository;
import org.example.demomanagementsystemcproject.service.OrderService;
import org.example.demomanagementsystemcproject.service.PointsRuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;
    private final PointsRuleService pointsRuleService;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                            ProductRepository productRepository,
                            AdminRepository adminRepository,
                            PointsRuleService pointsRuleService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.adminRepository = adminRepository;
        this.pointsRuleService = pointsRuleService;
    }

    @Override
    public Page<OrderDTO> getOrders(OrderQueryDTO query) {
        Pageable pageable = PageRequest.of(
                query.getPage() - 1,
                query.getSize(),
                Sort.by(Sort.Direction.DESC, "createdAt", "id")
        );

        Specification<OrderEntity> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (query.getOrderNo() != null && !query.getOrderNo().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("orderNo"), "%" + query.getOrderNo() + "%"));
            }

            if (query.getCustomerName() != null && !query.getCustomerName().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("customerName"), "%" + query.getCustomerName() + "%"));
            }

            if (query.getCustomerPhone() != null && !query.getCustomerPhone().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("customerPhone"), "%" + query.getCustomerPhone() + "%"));
            }

            if (query.getStatus() != null && !query.getStatus().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), query.getStatus()));
            }

            if (query.getPayStatus() != null && !query.getPayStatus().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("payStatus"), query.getPayStatus()));
            }

            if (query.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), query.getStartDate()));
            }

            if (query.getEndDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), query.getEndDate()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return orderRepository.findAll(spec, pageable).map(this::convertToDTO);
    }

    @Override
    @Transactional
    public OrderDTO createOrder(OrderDTO request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("订单项不能为空");
        }

        // 预加载商品信息，便于价格和名称兜底
        List<Long> productIds = request.getItems().stream()
                .map(OrderItemDTO::getProductId)
                .filter(id -> id != null)
                .distinct()
                .toList();
        var productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(ProductEntity::getId, p -> p));

        OrderEntity entity = new OrderEntity();
        entity.setOrderNo(generateOrderNo());
        entity.setCustomerName(request.getCustomerName());
        entity.setCustomerPhone(request.getCustomerPhone());
        entity.setCustomerAddress(request.getCustomerAddress());
        entity.setRemark(request.getRemark());
        entity.setUserOpenid(request.getUserOpenid());
        Long userId = resolveUserId(request.getUserId());
        if (userId == null) {
            throw new RuntimeException("订单需要关联用户信息");
        }
        entity.setUserId(userId);
        entity.setStatus("NEW");
        entity.setPayStatus("UNPAID");

        // 积分抵扣：后端兜底允许的最大抵扣 = 当前可用积分，防止超扣导致异常
        Admin admin = adminRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("找不到对应的用户"));
        Integer requestedPoints = request.getUsedPoints();
        if (requestedPoints == null || requestedPoints < 0) {
            requestedPoints = 0;
        }
        int currentAvailable = admin.getAvailablePoints() == null
                ? (admin.getPoints() == null ? 0 : admin.getPoints())
                : admin.getAvailablePoints();
        int allowedUsedPoints = Math.min(requestedPoints, currentAvailable);
        entity.setPointsUsed(allowedUsedPoints);

        BigDecimal goodsAmount = request.getGoodsAmount();
        BigDecimal discountAmount = request.getDiscountAmount();
        if (discountAmount == null) {
            discountAmount = request.getCouponDiscountAmount();
        }
        BigDecimal pointsDiscountAmount = calculatePointsDiscountAmount(allowedUsedPoints);

        BigDecimal computedGoods = BigDecimal.ZERO;
        List<OrderItemEntity> items = new ArrayList<>();
        Set<ProductEntity> productsToUpdate = new HashSet<>();
        for (OrderItemDTO itemRequest : request.getItems()) {
            OrderItemEntity item = new OrderItemEntity();
            item.setProductId(itemRequest.getProductId());

            ProductEntity product = itemRequest.getProductId() == null ? null : productMap.get(itemRequest.getProductId());
            String productName = itemRequest.getProductName();
            if (productName == null && product != null) {
                productName = product.getName();
            }
            item.setProductName(productName);

            BigDecimal price = itemRequest.getPrice();
            if (price == null && product != null) {
                price = product.getPrice();
            }
            item.setPrice(price);
            if (itemRequest.getQuantity() == null || itemRequest.getQuantity() <= 0) {
                throw new RuntimeException("订单项数量必须大于0");
            }
            item.setQuantity(itemRequest.getQuantity());

            if (product != null && product.getStock() != null) {
                int remaining = product.getStock() - itemRequest.getQuantity();
                product.setStock(Math.max(remaining, 0));
                productsToUpdate.add(product);
            }

            BigDecimal subtotal = itemRequest.getSubtotal();
            if (subtotal == null && price != null && itemRequest.getQuantity() != null) {
                subtotal = price.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            }
            if (subtotal == null) {
                subtotal = BigDecimal.ZERO;
            }

            item.setSubtotal(subtotal);
            computedGoods = computedGoods.add(subtotal);
            items.add(item);
        }

        if (goodsAmount == null) {
            goodsAmount = computedGoods;
        }
        if (discountAmount == null && request.getTotalAmount() != null) {
            BigDecimal candidate = goodsAmount.subtract(request.getTotalAmount());
            if (candidate.compareTo(BigDecimal.ZERO) < 0) {
                candidate = BigDecimal.ZERO;
            }
            discountAmount = candidate;
        }
        if (discountAmount == null) {
            discountAmount = BigDecimal.ZERO;
        }

        BigDecimal payAmount = request.getTotalAmount();
        BigDecimal computedPay = goodsAmount.subtract(discountAmount).subtract(pointsDiscountAmount);
        if (computedPay.compareTo(BigDecimal.ZERO) < 0) {
            computedPay = BigDecimal.ZERO;
        }
        if (payAmount == null) {
            payAmount = computedPay;
        } else if (payAmount.compareTo(computedPay) != 0) {
            // 后端强制以自身计算为准，防止前端篡改
            payAmount = computedPay;
        }
        if (payAmount.compareTo(BigDecimal.ZERO) < 0) {
            payAmount = BigDecimal.ZERO;
        }

        entity.setGoodsAmount(goodsAmount);
        entity.setDiscountAmount(discountAmount);
        entity.setCouponId(request.getCouponId());
        entity.setUserCouponId(request.getUserCouponId());
        entity.setCouponDiscountAmount(request.getCouponDiscountAmount());
        entity.setTotalAmount(payAmount);
        entity.setPointsDiscountAmount(pointsDiscountAmount);
        entity.setPayStatus("PAID");
        entity.setPaymentTime(LocalDateTime.now());
        if (entity.getPointsUsed() == null) {
            entity.setPointsUsed(0);
        }
        int earnedPoints = applyPointsSettlement(userId, payAmount, allowedUsedPoints);
        entity.setEarnedPoints(earnedPoints);

        OrderEntity saved = orderRepository.save(entity);

        for (OrderItemEntity item : items) {
            item.setOrderId(saved.getId());
        }
        orderItemRepository.saveAll(items);

        if (!productsToUpdate.isEmpty()) {
            productRepository.saveAll(productsToUpdate);
        }

        return convertToDTO(saved);
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        OrderEntity entity = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        return convertToDTO(entity);
    }

    @Override
    public OrderDTO getOrderByNo(String orderNo) {
        OrderEntity entity = orderRepository.findByOrderNo(orderNo);
        if (entity == null) {
            throw new RuntimeException("订单不存在");
        }
        return convertToDTO(entity);
    }

    @Override
    @Transactional
    public void confirmOrder(Long id) {
        OrderEntity entity = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!"NEW".equals(entity.getStatus())) {
            throw new RuntimeException("只能确认新订单");
        }

        entity.setStatus("CONFIRMED");
        entity.setConfirmedTime(LocalDateTime.now());
        orderRepository.save(entity);
    }

    @Override
    @Transactional
    public void batchConfirmOrders(List<Long> ids) {
        for (Long id : ids) {
            confirmOrder(id);
        }
    }

    @Override
    @Transactional
    public void completeOrder(Long id) {
        OrderEntity entity = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!"CONFIRMED".equals(entity.getStatus())) {
            throw new RuntimeException("只能完成已确认的订单");
        }

        entity.setStatus("COMPLETED");
        entity.setCompletedTime(LocalDateTime.now());
        orderRepository.save(entity);
    }

    @Override
    @Transactional
    public void cancelOrder(Long id, String reason) {
        OrderEntity entity = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if ("COMPLETED".equals(entity.getStatus()) || "CANCELLED".equals(entity.getStatus())) {
            throw new RuntimeException("不能取消已完成或已取消的订单");
        }

        entity.setStatus("CANCELLED");
        entity.setCancelledTime(LocalDateTime.now());
        orderRepository.save(entity);
    }

    @Override
    @Transactional
    public void requestRefund(RefundRequestDTO request) {
        OrderEntity entity = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!"COMPLETED".equals(entity.getStatus())) {
            throw new RuntimeException("只能对已完成的订单申请退款");
        }

        entity.setStatus("REFUNDING");
        entity.setRefundReason(request.getRefundReason());
        entity.setRefundAmount(request.getRefundAmount());
        orderRepository.save(entity);
    }

    @Override
    @Transactional
    public void approveRefund(Long orderId) {
        OrderEntity entity = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!"REFUNDING".equals(entity.getStatus())) {
            throw new RuntimeException("只能审核退款中的订单");
        }

        entity.setStatus("REFUNDED");
        entity.setPayStatus("REFUNDED");
        entity.setRefundTime(LocalDateTime.now());
        orderRepository.save(entity);
    }

    @Override
    @Transactional
    public void rejectRefund(Long orderId, String reason) {
        OrderEntity entity = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!"REFUNDING".equals(entity.getStatus())) {
            throw new RuntimeException("只能拒绝退款中的订单");
        }

        entity.setStatus("COMPLETED");
        entity.setRefundReason(reason);
        orderRepository.save(entity);
    }

    @Override
    public List<OrderDTO> getNewOrderAlerts() {
        LocalDateTime sinceTime = LocalDateTime.now().minusMinutes(5);
        List<OrderEntity> newOrders = orderRepository.findNewOrdersSince(sinceTime);
        return newOrders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] exportOrders(OrderQueryDTO query) {
        // 简化的导出实现 - 实际项目中可以使用Apache POI或EasyExcel
        List<OrderDTO> orders = getOrders(query).getContent();

        // 构建CSV内容
        StringBuilder csv = new StringBuilder();
        csv.append("订单号,客户姓名,手机号,金额,状态,支付状态,创建时间\n");

        for (OrderDTO order : orders) {
            csv.append(String.format("%s,%s,%s,%.2f,%s,%s,%s\n",
                    order.getOrderNo(),
                    order.getCustomerName() != null ? order.getCustomerName() : "",
                    order.getCustomerPhone() != null ? order.getCustomerPhone() : "",
                    order.getTotalAmount().doubleValue(),
                    order.getStatus(),
                    order.getPayStatus(),
                    order.getCreatedAt().toString()
            ));
        }

        return csv.toString().getBytes();
    }

    private int applyPointsSettlement(Long userId, BigDecimal actualAmount, Integer usedPoints) {
        if (userId == null) {
            throw new RuntimeException("订单需要关联用户信息");
        }
        if (actualAmount == null) {
            actualAmount = BigDecimal.ZERO;
        }

        Admin admin = adminRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("找不到对应的用户"));

        var activeRule = pointsRuleService.getActiveRule();
        Integer earnPerYuan = activeRule != null ? activeRule.getEarnPerYuan() : null;
        int ratio = earnPerYuan == null ? 10 : earnPerYuan;
        int earnedPoints = actualAmount.multiply(BigDecimal.valueOf(ratio)).setScale(0, RoundingMode.FLOOR).intValue();
        int used = usedPoints == null ? 0 : usedPoints;

        int currentLevel = admin.getLevelPoints() == null
                ? (admin.getPoints() == null ? 0 : admin.getPoints())
                : admin.getLevelPoints();
        int currentAvailable = admin.getAvailablePoints() == null
                ? (admin.getPoints() == null ? 0 : admin.getPoints())
                : admin.getAvailablePoints();

        if (used > currentAvailable) {
            throw new RuntimeException("积分不足");
        }

        admin.setLevelPoints(currentLevel + earnedPoints);
        admin.setAvailablePoints(Math.max(currentAvailable + earnedPoints - used, 0));
        admin.setPoints(admin.getAvailablePoints());
        adminRepository.save(admin);
        return earnedPoints;
    }

    private BigDecimal calculatePointsDiscountAmount(int usedPoints) {
        if (usedPoints <= 0) {
            return BigDecimal.ZERO;
        }

        var activeRule = pointsRuleService.getActiveRule();
        if (activeRule == null || activeRule.getRedeemPoints() == null || activeRule.getRedeemYuan() == null) {
            return BigDecimal.ZERO;
        }

        int redeemPoints = activeRule.getRedeemPoints();
        if (redeemPoints <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal redeemYuan = activeRule.getRedeemYuan();
        if (redeemYuan == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal groups = BigDecimal.valueOf(usedPoints)
                .divide(BigDecimal.valueOf(redeemPoints), 0, RoundingMode.DOWN);
        return redeemYuan.multiply(groups);
    }

    private Long resolveUserId(Long requestUserId) {
        if (requestUserId != null) {
            return requestUserId;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Admin currentUser) {
            return currentUser.getId();
        }

        return null;
    }

    private OrderDTO convertToDTO(OrderEntity entity) {
        OrderDTO dto = new OrderDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setUsedPoints(entity.getPointsUsed());
        dto.setPointsDiscountAmount(entity.getPointsDiscountAmount());
        dto.setEarnedPoints(entity.getEarnedPoints());

        // 加载订单项
        List<OrderItemEntity> items = orderItemRepository.findByOrderId(entity.getId());
        List<OrderItemDTO> itemDTOs = items.stream()
                .map(this::convertItemToDTO)
                .collect(Collectors.toList());
        dto.setItems(itemDTOs);

        return dto;
    }

    private OrderItemDTO convertItemToDTO(OrderItemEntity entity) {
        OrderItemDTO dto = new OrderItemDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    private String generateOrderNo() {
        return "OD" + System.currentTimeMillis();
    }
}
