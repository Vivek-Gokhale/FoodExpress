package com.foodexpress.admin.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Generates getters, setters, equals, hashCode, and toString methods
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor // Generates an all-args constructor
public class OrderPerformanceWeekly {

    private int year;
    private int weekNumber;
    private int totalOrders;
    private double totalOrderValue;
    private int completedOrders;
    private int pendingOrders;
    private int canceledOrders;

    // Method to map from Object array to OrderPerformanceWeekly
    public static OrderPerformanceWeekly fromObjectArray(Object[] data) {
        if (data == null || data.length < 7) {
            throw new IllegalArgumentException("Invalid data array");
        }
        return new OrderPerformanceWeekly(
            data[0] != null ? ((Number) data[0]).intValue() : 0,      // year
            data[1] != null ? ((Number) data[1]).intValue() : 0,      // weekNumber
            data[2] != null ? ((Number) data[2]).intValue() : 0,      // totalOrders
            data[3] != null ? ((Number) data[3]).doubleValue() : 0.0, // totalOrderValue
            data[4] != null ? ((Number) data[4]).intValue() : 0,      // completedOrders
            data[5] != null ? ((Number) data[5]).intValue() : 0,      // pendingOrders
            data[6] != null ? ((Number) data[6]).intValue() : 0       // canceledOrders
        );
    }

    // Method to map from List of Object arrays to List of OrderPerformanceWeekly
    public static List<OrderPerformanceWeekly> fromObjectArrayList(List<Object[]> dataList) {
        if (dataList == null) {
            throw new IllegalArgumentException("Data list cannot be null");
        }
        return dataList.stream()
                       .map(OrderPerformanceWeekly::fromObjectArray)
                       .collect(Collectors.toList());
    }
}
