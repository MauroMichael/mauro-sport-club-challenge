package com.sportclub.challenge.dto;

import com.sportclub.challenge.enums.OrderStatus;

import java.time.LocalDate;
import java.util.List;

public class CreateOrderRequestDto {

    private Long customerId;
    private LocalDate date;
    private OrderStatus status;
    private List<CreateOrderItemRequestDto> items;

    public CreateOrderRequestDto() {
    }

    public CreateOrderRequestDto(Long customerId, LocalDate date, OrderStatus status,
            List<CreateOrderItemRequestDto> items) {
        this.customerId = customerId;
        this.date = date;
        this.status = status;
        this.items = items;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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

    public List<CreateOrderItemRequestDto> getItems() {
        return items;
    }

    public void setItems(List<CreateOrderItemRequestDto> items) {
        this.items = items;
    }
}