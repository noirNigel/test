package org.example.demomanagementsystemcproject.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "member_level")
public class MemberLevelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "min_points")
    private Integer minPoints = 0;

    @Column(name = "discount_rate")
    private BigDecimal discountRate = BigDecimal.ONE;

    @Column(name = "points_multiplier")
    private BigDecimal pointsMultiplier = BigDecimal.ONE;

    @Column(name = "birthday_benefit")
    private String birthdayBenefit;

    @Column(name = "benefits", columnDefinition = "TEXT")
    private String benefits;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    private Integer status = 1;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getMinPoints() { return minPoints; }
    public void setMinPoints(Integer minPoints) { this.minPoints = minPoints; }
    public BigDecimal getDiscountRate() { return discountRate; }
    public void setDiscountRate(BigDecimal discountRate) { this.discountRate = discountRate; }
    public BigDecimal getPointsMultiplier() { return pointsMultiplier; }
    public void setPointsMultiplier(BigDecimal pointsMultiplier) { this.pointsMultiplier = pointsMultiplier; }
    public String getBirthdayBenefit() { return birthdayBenefit; }
    public void setBirthdayBenefit(String birthdayBenefit) { this.birthdayBenefit = birthdayBenefit; }
    public String getBenefits() { return benefits; }
    public void setBenefits(String benefits) { this.benefits = benefits; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}