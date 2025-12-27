package org.example.demomanagementsystemcproject.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PromotionDTO {
    private Long id;
    private String name;
    private String type;
    private BigDecimal conditionAmount;
    private BigDecimal reduceAmount;
    private BigDecimal discountRate;
    private String productIds;
    private String categoryIds;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 扩展字段
    private String statusText;
    private List<Long> productIdList;
    private List<Long> categoryIdList;
    private Boolean isActive; // 是否进行中

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public BigDecimal getConditionAmount() { return conditionAmount; }
    public void setConditionAmount(BigDecimal conditionAmount) { this.conditionAmount = conditionAmount; }
    public BigDecimal getReduceAmount() { return reduceAmount; }
    public void setReduceAmount(BigDecimal reduceAmount) { this.reduceAmount = reduceAmount; }
    public BigDecimal getDiscountRate() { return discountRate; }
    public void setDiscountRate(BigDecimal discountRate) { this.discountRate = discountRate; }
    public String getProductIds() { return productIds; }
    public void setProductIds(String productIds) { this.productIds = productIds; }
    public String getCategoryIds() { return categoryIds; }
    public void setCategoryIds(String categoryIds) { this.categoryIds = categoryIds; }
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
    public List<Long> getProductIdList() { return productIdList; }
    public void setProductIdList(List<Long> productIdList) { this.productIdList = productIdList; }
    public List<Long> getCategoryIdList() { return categoryIdList; }
    public void setCategoryIdList(List<Long> categoryIdList) { this.categoryIdList = categoryIdList; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}