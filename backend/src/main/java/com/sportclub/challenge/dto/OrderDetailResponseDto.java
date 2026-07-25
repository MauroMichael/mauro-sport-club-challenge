package com.sportclub.challenge.dto;

import com.sportclub.challenge.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OrderDetailResponseDto {

    private Long id;
    private LocalDate date;
    private OrderStatus status;
    private BigDecimal total;
    private String customerName;
    private String customerEmail;
    private List<OrderItemResponseDto> items;

    public OrderDetailResponseDto() {
    }

    public OrderDetailResponseDto(Long id, LocalDate date, OrderStatus status, BigDecimal total, String customerName,
            String customerEmail, List<OrderItemResponseDto> items) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.total = total;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.items = items;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public List<OrderItemResponseDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponseDto> items) {
        this.items = items;
    }
}
