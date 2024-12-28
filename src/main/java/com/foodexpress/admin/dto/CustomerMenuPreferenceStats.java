package com.foodexpress.admin.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerMenuPreferenceStats {

    private String dishName;
    private long orderCount;
    private List<String> customizations; 
    private double percentageOfTotalOrders;
}
