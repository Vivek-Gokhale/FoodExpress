package com.foodexpress.customer.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "coupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id", nullable = false, updatable = false)
    private Integer couponId;

    @Column(name = "code", nullable = false, unique = true, length = 100)
    private String code;

    @Column(name = "percentage", nullable = false)
    private Integer percentage;

    @Column(name = "min_order_value", nullable = false)
    private Integer minOrderValue;

    @Column(name = "max_discount_value", nullable = false)
    private Integer maxDiscountValue;

    @Column(name = "usage_count", nullable = false)
    private Integer usageCount;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
}