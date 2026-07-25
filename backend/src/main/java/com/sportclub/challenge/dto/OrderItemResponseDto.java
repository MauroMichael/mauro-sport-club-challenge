package com.sportclub.challenge.dto;

import java.math.BigDecimal;

public class OrderItemResponseDto {
    private String productName;
    private Integer quantity;
    private BigDecimal price;

    public OrderItemResponseDto() {
    }

    public OrderItemResponseDto(String productName, Integer quantity, BigDecimal price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
