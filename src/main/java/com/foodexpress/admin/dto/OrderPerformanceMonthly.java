package com.foodexpress.admin.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Generates getters, setters, equals, hashCode, and toString methods
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor // Generates an all-args constructor
public class OrderPerformanceMonthly {

    private int year;
    private int month;
    private int totalOrders;
    private double totalOrderValue;
    private int completedOrders;
    private int pendingOrders;
    private int canceledOrders;

    public static OrderPerformanceMonthly fromObjectArray(Object[] data) {
        if (data == null || data.length < 7) {
            throw new IllegalArgumentException("Invalid data array");
        }
        return new OrderPerformanceMonthly(
            data[0] != null ? ((Number) data[0]).intValue() : 0,        // year
            data[1] != null ? ((Number) data[1]).intValue() : 0,        // month
            data[2] != null ? ((Number) data[2]).intValue() : 0,        // totalOrders
            data[3] != null ? ((Number) data[3]).doubleValue() : 0.0,   // totalOrderValue
            data[4] != null ? ((Number) data[4]).intValue() : 0,        // completedOrders
            data[5] != null ? ((Number) data[5]).intValue() : 0,        // pendingOrders
            data[6] != null ? ((Number) data[6]).intValue() : 0         // canceledOrders
        );
    }

    public static List<OrderPerformanceMonthly> fromObjectArrayList(List<Object[]> dataList) {
        if (dataList == null) {
            throw new IllegalArgumentException("Data list cannot be null");
        }
        return dataList.stream()
                       .map(OrderPerformanceMonthly::fromObjectArray)
                       .collect(Collectors.toList());
    }
}
