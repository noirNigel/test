package org.example.demomanagementsystemcproject.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
    private Long id;
    private String orderNo;
    private String userOpenid;
    private Long userId;
    private BigDecimal totalAmount;
    private String status;
    private String payStatus;
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private String remark;

    private Integer usedPoints;

    // 价格与优惠信息
    private BigDecimal goodsAmount;
    private BigDecimal discountAmount;
    private BigDecimal pointsDiscountAmount;
    private Long couponId;
    private Long userCouponId;
    private BigDecimal couponDiscountAmount;
    private Integer earnedPoints;
    private LocalDateTime paymentTime;
    private LocalDateTime confirmedTime;
    private LocalDateTime completedTime;
    private LocalDateTime cancelledTime;
    private String refundReason;
    private BigDecimal refundAmount;
    private LocalDateTime refundTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<OrderItemDTO> items;

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    public String getUserOpenid() { return userOpenid; }
    public void setUserOpenid(String userOpenid) { this.userOpenid = userOpenid; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPayStatus() { return payStatus; }
    public void setPayStatus(String payStatus) { this.payStatus = payStatus; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCustomerAddress() { return customerAddress; }
    public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public Integer getUsedPoints() { return usedPoints; }
    public void setUsedPoints(Integer usedPoints) { this.usedPoints = usedPoints; }

    public BigDecimal getGoodsAmount() { return goodsAmount; }
    public void setGoodsAmount(BigDecimal goodsAmount) { this.goodsAmount = goodsAmount; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public BigDecimal getPointsDiscountAmount() { return pointsDiscountAmount; }
    public void setPointsDiscountAmount(BigDecimal pointsDiscountAmount) { this.pointsDiscountAmount = pointsDiscountAmount; }

    public Long getCouponId() { return couponId; }
    public void setCouponId(Long couponId) { this.couponId = couponId; }

    public Long getUserCouponId() { return userCouponId; }
    public void setUserCouponId(Long userCouponId) { this.userCouponId = userCouponId; }

    public BigDecimal getCouponDiscountAmount() { return couponDiscountAmount; }
    public void setCouponDiscountAmount(BigDecimal couponDiscountAmount) { this.couponDiscountAmount = couponDiscountAmount; }

    public Integer getEarnedPoints() { return earnedPoints; }
    public void setEarnedPoints(Integer earnedPoints) { this.earnedPoints = earnedPoints; }

    public LocalDateTime getPaymentTime() { return paymentTime; }
    public void setPaymentTime(LocalDateTime paymentTime) { this.paymentTime = paymentTime; }

    public LocalDateTime getConfirmedTime() { return confirmedTime; }
    public void setConfirmedTime(LocalDateTime confirmedTime) { this.confirmedTime = confirmedTime; }

    public LocalDateTime getCompletedTime() { return completedTime; }
    public void setCompletedTime(LocalDateTime completedTime) { this.completedTime = completedTime; }

    public LocalDateTime getCancelledTime() { return cancelledTime; }
    public void setCancelledTime(LocalDateTime cancelledTime) { this.cancelledTime = cancelledTime; }

    public String getRefundReason() { return refundReason; }
    public void setRefundReason(String refundReason) { this.refundReason = refundReason; }

    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }

    public LocalDateTime getRefundTime() { return refundTime; }
    public void setRefundTime(LocalDateTime refundTime) { this.refundTime = refundTime; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<OrderItemDTO> getItems() { return items; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }
}