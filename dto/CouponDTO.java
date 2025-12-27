package org.example.demomanagementsystemcproject.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CouponDTO {
    private Long id;
    private String name;
    private String type;
    private BigDecimal discount;
    private BigDecimal minAmount;
    private BigDecimal reduceAmount;
    private Integer totalCount;
    private Integer remainingCount;
    private Integer limitPerUser;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 状态文本
    private String statusText;
    // 是否可领取
    private Boolean canClaim;
    // 用户已领取数量
    private Integer userClaimedCount;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }
    public BigDecimal getMinAmount() { return minAmount; }
    public void setMinAmount(BigDecimal minAmount) { this.minAmount = minAmount; }
    public BigDecimal getReduceAmount() { return reduceAmount; }
    public void setReduceAmount(BigDecimal reduceAmount) { this.reduceAmount = reduceAmount; }
    public Integer getTotalCount() { return totalCount; }
    public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }
    public Integer getRemainingCount() { return remainingCount; }
    public void setRemainingCount(Integer remainingCount) { this.remainingCount = remainingCount; }
    public Integer getLimitPerUser() { return limitPerUser; }
    public void setLimitPerUser(Integer limitPerUser) { this.limitPerUser = limitPerUser; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public String getStatusText() { return statusText; }
    public void setStatusText(String statusText) { this.statusText = statusText; }
    public Boolean getCanClaim() { return canClaim; }
    public void setCanClaim(Boolean canClaim) { this.canClaim = canClaim; }
    public Integer getUserClaimedCount() { return userClaimedCount; }
    public void setUserClaimedCount(Integer userClaimedCount) { this.userClaimedCount = userClaimedCount; }
}