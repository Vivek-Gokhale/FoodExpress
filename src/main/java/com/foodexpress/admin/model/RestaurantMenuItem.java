package com.foodexpress.admin.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Entity
@Table(name = "menu_items")  // Specifies the table name
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantMenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment for item_id
    @Column(name = "item_id")
    private int itemId;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock;

    @Column(length = 1000)
    private String description;

    @Column(length = 1000)
    private String images;

    @Column(name = "restaurant_id", nullable = false)
    private int restaurantId;
}
