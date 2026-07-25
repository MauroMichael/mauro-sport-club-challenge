package com.sportclub.challenge.dto;

import com.sportclub.challenge.enums.OrderStatus;

public class UpdateOrderStatusRequestDto {

    private OrderStatus status;

    public UpdateOrderStatusRequestDto() {
    }

    public UpdateOrderStatusRequestDto(OrderStatus status) {
        this.status = status;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
