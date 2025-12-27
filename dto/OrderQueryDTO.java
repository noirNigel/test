package org.example.demomanagementsystemcproject.dto;

import java.time.LocalDateTime;

public class OrderQueryDTO {
    private String orderNo;
    private String customerName;
    private String customerPhone;
    private String status;
    private String payStatus;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer page = 1;
    private Integer size = 10;

    // getters and setters
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPayStatus() { return payStatus; }
    public void setPayStatus(String payStatus) { this.payStatus = payStatus; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }

    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
}