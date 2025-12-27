package org.example.demomanagementsystemcproject.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "points_rule")
public class PointsRuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "earn_per_yuan", nullable = false)
    private Integer earnPerYuan = 10;

    @Column(name = "redeem_points", nullable = false)
    private Integer redeemPoints = 100;

    @Column(name = "redeem_yuan", nullable = false, precision = 10, scale = 2)
    private BigDecimal redeemYuan = BigDecimal.ONE;

    @Column(nullable = false)
    private Integer status = 1;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (earnPerYuan == null) {
            earnPerYuan = 10;
        }
        if (redeemPoints == null) {
            redeemPoints = 100;
        }
        if (redeemYuan == null) {
            redeemYuan = BigDecimal.ONE;
        }
        if (status == null) {
            status = 1;
        }
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEarnPerYuan() {
        return earnPerYuan;
    }

    public void setEarnPerYuan(Integer earnPerYuan) {
        this.earnPerYuan = earnPerYuan;
    }

    public Integer getRedeemPoints() {
        return redeemPoints;
    }

    public void setRedeemPoints(Integer redeemPoints) {
        this.redeemPoints = redeemPoints;
    }

    public BigDecimal getRedeemYuan() {
        return redeemYuan;
    }

    public void setRedeemYuan(BigDecimal redeemYuan) {
        this.redeemYuan = redeemYuan;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
