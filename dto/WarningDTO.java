package org.example.demomanagementsystemcproject.dto;


public class WarningDTO {
    private Long productId;
    private String productName;
    private Integer quantity;
    private Integer threshold;

    public WarningDTO() {
    }

    public WarningDTO(Long productId, String productName, Integer quantity, Integer threshold) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.threshold = threshold;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }
}
