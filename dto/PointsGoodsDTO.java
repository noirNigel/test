package org.example.demomanagementsystemcproject.dto;

import java.math.BigDecimal;

public class PointsGoodsDTO {
    private Long id;
    private String name;
    private String image;
    private Integer pointsRequired;
    private BigDecimal originalPrice;
    private Integer stock;
    private Integer totalStock;
    private Integer status;
    private String description;
    private Integer sortOrder;

    // 扩展字段
    private String statusText;
    private Boolean canExchange; // 是否可以兑换

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public Integer getPointsRequired() { return pointsRequired; }
    public void setPointsRequired(Integer pointsRequired) { this.pointsRequired = pointsRequired; }
    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Integer getTotalStock() { return totalStock; }
    public void setTotalStock(Integer totalStock) { this.totalStock = totalStock; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getStatusText() { return statusText; }
    public void setStatusText(String statusText) { this.statusText = statusText; }
    public Boolean getCanExchange() { return canExchange; }
    public void setCanExchange(Boolean canExchange) { this.canExchange = canExchange; }
}