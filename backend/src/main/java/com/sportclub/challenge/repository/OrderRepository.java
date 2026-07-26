package com.sportclub.challenge.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sportclub.challenge.entity.Order;
import com.sportclub.challenge.enums.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Override
    @EntityGraph(attributePaths = {"customer", "orderItems"})
    Optional<Order> findById(Long id);

    @EntityGraph(attributePaths = "customer")
    @Query("""
    SELECT o
    FROM Order o
    WHERE (:status IS NULL OR o.status = :status)
      AND (:dateFrom IS NULL OR o.date >= :dateFrom)
      AND (:dateTo IS NULL OR o.date <= :dateTo)
    """)
    Page<Order> findByFilters(
            @Param("status") OrderStatus status,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo,
            Pageable pageable);
}
