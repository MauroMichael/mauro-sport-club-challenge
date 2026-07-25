package com.sportclub.challenge.repository;

import com.sportclub.challenge.entity.Order;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Override
    @EntityGraph(attributePaths = "customer")
    List<Order> findAll();

    @Override
    @EntityGraph(attributePaths = { "customer", "orderItems" })
    Optional<Order> findById(Long id);
}