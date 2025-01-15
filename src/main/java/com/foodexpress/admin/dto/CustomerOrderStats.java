package com.foodexpress.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerOrderStats {

    private int userId;
    private double totalOrders;
    private double ordersInLast7Days;
    private double ordersInLast30Days;
    private double ordersInLast365Days;

    // New fields for average orders
    private double avgOrdersPerWeek;
    private double avgOrdersPerMonth;
    private double avgOrdersPerYear;
}
