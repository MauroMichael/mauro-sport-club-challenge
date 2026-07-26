package com.sportclub.challenge.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sportclub.challenge.dto.CreateOrderItemRequestDto;
import com.sportclub.challenge.dto.CreateOrderRequestDto;
import com.sportclub.challenge.dto.OrderDetailResponseDto;
import com.sportclub.challenge.dto.OrderItemResponseDto;
import com.sportclub.challenge.dto.OrderResponseDto;
import com.sportclub.challenge.dto.PageResponseDto;
import com.sportclub.challenge.dto.UpdateOrderStatusRequestDto;
import com.sportclub.challenge.entity.Customer;
import com.sportclub.challenge.entity.Order;
import com.sportclub.challenge.entity.OrderItem;
import com.sportclub.challenge.enums.OrderStatus;
import com.sportclub.challenge.repository.CustomerRepository;
import com.sportclub.challenge.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    public PageResponseDto<OrderResponseDto> getAllOrders(OrderStatus status, String dateFrom, String dateTo, int page, int size) {
        LocalDate parsedDateFrom = dateFrom != null && !dateFrom.isBlank() ? LocalDate.parse(dateFrom) : null;
        LocalDate parsedDateTo = dateTo != null && !dateTo.isBlank() ? LocalDate.parse(dateTo) : null;

        if (parsedDateFrom != null && parsedDateTo != null && parsedDateFrom.isAfter(parsedDateTo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dateFrom cannot be after dateTo");
        }

        if (page < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "page must be greater than or equal to 0");
        }

        if (size <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "size must be greater than 0");
        }

        if (size > 50) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "size must be less than or equal to 50");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("date"), Sort.Order.desc("id")));

        Page<Order> ordersPage = orderRepository.findByFilters(status, parsedDateFrom, parsedDateTo, pageable);

        List<OrderResponseDto> content = ordersPage.getContent()
                .stream()
                .map(this::mapToDto)
                .toList();

        return new PageResponseDto<>(
                content,
                ordersPage.getNumber(),
                ordersPage.getSize(),
                ordersPage.getTotalElements(),
                ordersPage.getTotalPages(),
                ordersPage.isLast()
        );
    }

    private OrderResponseDto mapToDto(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getDate(),
                order.getStatus(),
                order.getTotal(),
                order.getCustomer().getName());
    }

    public OrderDetailResponseDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        return new OrderDetailResponseDto(
                order.getId(),
                order.getDate(),
                order.getStatus(),
                order.getTotal(),
                order.getCustomer().getName(),
                order.getCustomer().getEmail(),
                order.getOrderItems().stream()
                        .map(item -> new OrderItemResponseDto(
                        item.getProductName(),
                        item.getQuantity(),
                        item.getPrice()))
                        .toList());
    }

    public OrderDetailResponseDto updateOrderStatus(Long id, UpdateOrderStatusRequestDto request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (order.getStatus() == OrderStatus.PAID || order.getStatus() == OrderStatus.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cannot change status of a paid or cancelled order");
        }

        order.setStatus(request.getStatus());

        orderRepository.save(order);

        return getOrderById(id);
    }

    public OrderDetailResponseDto createOrder(CreateOrderRequestDto request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        Order order = new Order();
        order.setDate(request.getDate());
        order.setStatus(request.getStatus());
        order.setCustomer(customer);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CreateOrderItemRequestDto itemRequest : request.getItems()) {
            OrderItem item = new OrderItem();
            item.setProductName(itemRequest.getProductName());
            item.setQuantity(itemRequest.getQuantity());
            item.setPrice(itemRequest.getPrice());
            item.setOrder(order);

            orderItems.add(item);

            BigDecimal itemSubtotal = itemRequest.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            total = total.add(itemSubtotal);
        }
        order.setOrderItems(orderItems);
        order.setTotal(total);
        Order savedOrder = orderRepository.save(order);
        return getOrderById(savedOrder.getId());
    }
}
