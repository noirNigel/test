package org.example.demomanagementsystemcproject.dto;

import java.math.BigDecimal;

public class RefundRequestDTO {
    private Long orderId;
    private String refundReason;
    private BigDecimal refundAmount;

    // getters and setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getRefundReason() { return refundReason; }
    public void setRefundReason(String refundReason) { this.refundReason = refundReason; }

    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }
}