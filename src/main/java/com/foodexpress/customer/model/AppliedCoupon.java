package com.foodexpress.customer.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "applied_coupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppliedCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "applied_coupons_id", nullable = false, updatable = false)
    private Integer appliedCouponsId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "coupon_ids", nullable = false, length = 11)
    private Integer couponIds;
}