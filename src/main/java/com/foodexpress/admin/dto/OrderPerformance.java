package com.foodexpress.admin.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Generates getters, setters, equals, hashCode, and toString methods
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor // Generates an all-args constructor
public class OrderPerformance {
	 private String reportDate;
	    private int totalOrders;
	    private double totalOrderValue;
	    private int completedOrders;
	    private int pendingOrders;
	    private int canceledOrders;
	    
	    public static OrderPerformance fromObjectArray(Object[] data) {
	        if (data == null || data.length < 6) {
	            throw new IllegalArgumentException("Invalid data array");
	        }
	        return new OrderPerformance(
	            data[0] != null ? data[0].toString() : null,              // reportDate
	            data[1] != null ? ((Number) data[1]).intValue() : 0,     // totalOrders
	            data[2] != null ? ((Number) data[2]).doubleValue() : 0,  // totalOrderValue
	            data[3] != null ? ((Number) data[3]).intValue() : 0,     // completedOrders
	            data[4] != null ? ((Number) data[4]).intValue() : 0,     // pendingOrders
	            data[5] != null ? ((Number) data[5]).intValue() : 0      // canceledOrders
	        );
	    }
	    
	    public static List<OrderPerformance> fromObjectArrayList(List<Object[]> dataList) {
	        if (dataList == null) {
	            throw new IllegalArgumentException("Data list cannot be null");
	        }
	        return dataList.stream()
	                       .map(OrderPerformance::fromObjectArray)
	                       .collect(Collectors.toList());
	    }
}
