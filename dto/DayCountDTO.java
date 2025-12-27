package org.example.demomanagementsystemcproject.dto;


import java.math.BigDecimal;
import java.time.LocalDate;

public class DayCountDTO {
    private LocalDate day;
    private long orderCount;
    private BigDecimal sales;

    public DayCountDTO() {
    }

    public DayCountDTO(LocalDate day, long orderCount, BigDecimal sales) {
        this.day = day;
        this.orderCount = orderCount;
        this.sales = sales;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(long orderCount) {
        this.orderCount = orderCount;
    }

    public BigDecimal getSales() {
        return sales;
    }

    public void setSales(BigDecimal sales) {
        this.sales = sales;
    }
}
