package org.example.demomanagementsystemcproject.dto;

import java.math.BigDecimal;

public class PointsRuleDTO {

    private Long id;

    /** 消费 1 元可获得的积分数 */
    private Integer earnPerYuan;

    /** 兑换所需积分 */
    private Integer redeemPoints;

    /** 兑换积分可抵扣的金额 */
    private BigDecimal redeemYuan;

    private Integer status;

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
}
