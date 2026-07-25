package com.sportclub.challenge.dto;

import com.sportclub.challenge.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OrderResponseDto {

    private Long id;
    private LocalDate date;
    private OrderStatus status;
    private BigDecimal total;
    private String customerName;

    public OrderResponseDto() {
    }

    public OrderResponseDto(Long id, LocalDate date, OrderStatus status, BigDecimal total, String customerName) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.total = total;
        this.customerName = customerName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getCustomer() {
        return customerName;
    }

    public void setCustomer(String customerName) {
        this.customerName = customerName;
    }
}
