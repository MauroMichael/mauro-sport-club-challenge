package com.sportclub.challenge.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sportclub.challenge.dto.CreateOrderRequestDto;
import com.sportclub.challenge.dto.OrderDetailResponseDto;
import com.sportclub.challenge.dto.OrderResponseDto;
import com.sportclub.challenge.dto.PageResponseDto;
import com.sportclub.challenge.dto.UpdateOrderStatusRequestDto;
import com.sportclub.challenge.enums.OrderStatus;
import com.sportclub.challenge.service.OrderService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public PageResponseDto<OrderResponseDto> getAllOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return orderService.getAllOrders(status, dateFrom, dateTo, page, size);
    }

    @PostMapping
    public OrderDetailResponseDto createOrder(@RequestBody CreateOrderRequestDto request) {
        return orderService.createOrder(request);
    }

    @GetMapping("/{id}")
    public OrderDetailResponseDto getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PatchMapping("/{id}/status")
    public OrderDetailResponseDto updateOrderStatus(@PathVariable Long id,
            @RequestBody UpdateOrderStatusRequestDto request) {
        return orderService.updateOrderStatus(id, request);
    }

}
