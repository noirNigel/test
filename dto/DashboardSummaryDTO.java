package org.example.demomanagementsystemcproject.dto;

import java.math.BigDecimal;

public class DashboardSummaryDTO {

    private long ordersToday;
    private BigDecimal salesToday;
    private BigDecimal avgOrderValue;

    public DashboardSummaryDTO() {
    }

    public DashboardSummaryDTO(long ordersToday, BigDecimal salesToday, BigDecimal avgOrderValue) {
        this.ordersToday = ordersToday;
        this.salesToday = salesToday;
        this.avgOrderValue = avgOrderValue;
    }

    public long getOrdersToday() {
        return ordersToday;
    }

    public void setOrdersToday(long ordersToday) {
        this.ordersToday = ordersToday;
    }

    public BigDecimal getSalesToday() {
        return salesToday;
    }

    public void setSalesToday(BigDecimal salesToday) {
        this.salesToday = salesToday;
    }

    public BigDecimal getAvgOrderValue() {
        return avgOrderValue;
    }

    public void setAvgOrderValue(BigDecimal avgOrderValue) {
        this.avgOrderValue = avgOrderValue;
    }
}
