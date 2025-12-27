package org.example.demomanagementsystemcproject.dto;


import java.math.BigDecimal;

public class ProductRankDTO {
    private Long productId;
    private String productName;
    private Long soldQuantity;
    private BigDecimal revenue;

    public ProductRankDTO() {
    }

    public ProductRankDTO(Long productId, String productName, Long soldQuantity, BigDecimal revenue) {
        this.productId = productId;
        this.productName = productName;
        this.soldQuantity = soldQuantity;
        this.revenue = revenue;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(Long soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }
}
