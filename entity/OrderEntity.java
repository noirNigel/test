package org.example.demomanagementsystemcproject.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", nullable = false, unique = true)
    private String orderNo;

    @Column(name = "user_openid")
    private String userOpenid;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "goods_amount", precision = 12, scale = 2)
    private BigDecimal goodsAmount;

    @Column(name = "discount_amount", precision = 12, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "coupon_id")
    private Long couponId;

    @Column(name = "user_coupon_id")
    private Long userCouponId;

    @Column(name = "coupon_discount_amount", precision = 12, scale = 2)
    private BigDecimal couponDiscountAmount;

    @Column(name = "status")
    private String status; // NEW, PAID, CONFIRMED, COMPLETED, CANCELLED, REFUNDING, REFUNDED

    @Column(name = "pay_status")
    private String payStatus; // UNPAID, PAID, REFUNDED

    @Column(name = "points_used")
    private Integer pointsUsed;

    @Column(name = "points_discount_amount", precision = 12, scale = 2)
    private BigDecimal pointsDiscountAmount;

    @Column(name = "earned_points")
    private Integer earnedPoints;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "customer_address")
    private String customerAddress;

    @Column(name = "remark")
    private String remark;

    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    @Column(name = "confirmed_time")
    private LocalDateTime confirmedTime;

    @Column(name = "completed_time")
    private LocalDateTime completedTime;

    @Column(name = "cancelled_time")
    private LocalDateTime cancelledTime;

    @Column(name = "refund_reason")
    private String refundReason;

    @Column(name = "refund_amount", precision = 12, scale = 2)
    private BigDecimal refundAmount;

    @Column(name = "refund_time")
    private LocalDateTime refundTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // getters and setters
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = "NEW";
        }
        if (payStatus == null) {
            payStatus = "UNPAID";
        }
        if (pointsUsed == null) {
            pointsUsed = 0;
        }
        if (pointsDiscountAmount == null) {
            pointsDiscountAmount = BigDecimal.ZERO;
        }
        if (earnedPoints == null) {
            earnedPoints = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

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

    public BigDecimal getGoodsAmount() { return goodsAmount; }
    public void setGoodsAmount(BigDecimal goodsAmount) { this.goodsAmount = goodsAmount; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public Long getCouponId() { return couponId; }
    public void setCouponId(Long couponId) { this.couponId = couponId; }

    public Long getUserCouponId() { return userCouponId; }
    public void setUserCouponId(Long userCouponId) { this.userCouponId = userCouponId; }

    public BigDecimal getCouponDiscountAmount() { return couponDiscountAmount; }
    public void setCouponDiscountAmount(BigDecimal couponDiscountAmount) { this.couponDiscountAmount = couponDiscountAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPayStatus() { return payStatus; }
    public void setPayStatus(String payStatus) { this.payStatus = payStatus; }

    public Integer getPointsUsed() { return pointsUsed; }
    public void setPointsUsed(Integer pointsUsed) { this.pointsUsed = pointsUsed; }

    public BigDecimal getPointsDiscountAmount() { return pointsDiscountAmount; }
    public void setPointsDiscountAmount(BigDecimal pointsDiscountAmount) { this.pointsDiscountAmount = pointsDiscountAmount; }

    public Integer getEarnedPoints() { return earnedPoints; }
    public void setEarnedPoints(Integer earnedPoints) { this.earnedPoints = earnedPoints; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCustomerAddress() { return customerAddress; }
    public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

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
}