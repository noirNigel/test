package org.example.demomanagementsystemcproject.dto;

import java.time.LocalDateTime;

public class UserCouponDTO {
    private Long id;
    private Long userId;
    private Long couponId;
    private String status;
    private LocalDateTime getTime;
    private LocalDateTime useTime;
    private Long orderId;
    private LocalDateTime expiredTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 关联信息
    private CouponDTO coupon;
    private String statusText;
    private Boolean isExpired;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getCouponId() { return couponId; }
    public void setCouponId(Long couponId) { this.couponId = couponId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getGetTime() { return getTime; }
    public void setGetTime(LocalDateTime getTime) { this.getTime = getTime; }
    public LocalDateTime getUseTime() { return useTime; }
    public void setUseTime(LocalDateTime useTime) { this.useTime = useTime; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public LocalDateTime getExpiredTime() { return expiredTime; }
    public void setExpiredTime(LocalDateTime expiredTime) { this.expiredTime = expiredTime; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public CouponDTO getCoupon() { return coupon; }
    public void setCoupon(CouponDTO coupon) { this.coupon = coupon; }
    public String getStatusText() { return statusText; }
    public void setStatusText(String statusText) { this.statusText = statusText; }
    public Boolean getIsExpired() { return isExpired; }
    public void setIsExpired(Boolean isExpired) { this.isExpired = isExpired; }
}