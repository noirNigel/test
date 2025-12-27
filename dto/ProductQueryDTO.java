package org.example.demomanagementsystemcproject.dto;

public class ProductQueryDTO {
    private String name;
    private String sku;
    private Long categoryId;
    private Integer status;
    private Integer lowStock; // 1-低库存，0-正常库存
    private Integer page = 1;
    private Integer size = 10;

    // getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getLowStock() { return lowStock; }
    public void setLowStock(Integer lowStock) { this.lowStock = lowStock; }

    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }

    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
}