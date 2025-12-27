package org.example.demomanagementsystemcproject.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.math.BigDecimal;

public class ProductDTO {
    private Long id;
    private String name;
    private String sku;
    private BigDecimal price;
    private BigDecimal cost;
    private Integer stock;
    private Integer warningThreshold;
    private Integer status;
    private Long categoryId;
    private String categoryName;
    private String categoryPath;
    private String recipe;

    /** 商品轮播图（JSON 数组字符串） */
    private String images;

    @JsonAlias({"imageUrl", "image_url"})
    private String image;

    @JsonAlias("desc")
    private String description;

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Integer getWarningThreshold() { return warningThreshold; }
    public void setWarningThreshold(Integer warningThreshold) { this.warningThreshold = warningThreshold; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getCategoryPath() { return categoryPath; }
    public void setCategoryPath(String categoryPath) { this.categoryPath = categoryPath; }

    public String getRecipe() { return recipe; }
    public void setRecipe(String recipe) { this.recipe = recipe; }

    public String getImages() { return images; }
    public void setImages(String images) { this.images = images; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}