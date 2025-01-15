package com.foodexpress.admin.model;

import com.foodexpress.customer.model.Order;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer orderId;

    @Column(nullable = false)
    private Integer itemId;

    @Column(nullable = false)
    private Integer restaurantId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer price;

    @Column(length = 255)
    private String specialRequest;

    @Column(length = 20)
    private String currStatus;
    
    @Transient
    private String restaurantName;
    
    @Transient
    private String itemName;
    
    @Transient
    private String image;
}
