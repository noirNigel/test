package org.example.demomanagementsystemcproject.dto;

import java.math.BigDecimal;

public class MemberLevelDTO {
    private Long id;
    private String name;
    private Integer minPoints;
    private BigDecimal discountRate;
    private BigDecimal pointsMultiplier;
    private String birthdayBenefit;
    private String benefits;
    private Integer sortOrder;
    private Integer status;

    // 扩展字段
    private String statusText;
    private Integer memberCount; // 该等级会员数量

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
    public String getStatusText() { return statusText; }
    public void setStatusText(String statusText) { this.statusText = statusText; }
    public Integer getMemberCount() { return memberCount; }
    public void setMemberCount(Integer memberCount) { this.memberCount = memberCount; }
}