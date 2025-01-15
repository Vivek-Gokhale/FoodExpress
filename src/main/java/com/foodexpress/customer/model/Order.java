package com.foodexpress.customer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

import com.foodexpress.admin.model.OrderItem;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders") // Matches the table name in the database
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "order_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date orderDate;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "coupon_ids", length = 255)
    private String couponIds;

    @Column(name = "payment_option", nullable = false, length = 100)
    private String paymentOption;

    @Column(name = "time_stamp", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date timeStamp;

    @Column(name = "delivery_status", nullable = false, length = 100)
    private String deliveryStatus;

    // Add a transient field for OrderItem
    @Transient
    private List<OrderItem> orderItems; // Not persisted in the database
}
