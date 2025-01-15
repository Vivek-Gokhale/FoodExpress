package com.foodexpress.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSummary {

    private double totalAmount;
    private double couponDiscount;
    private double tax = 5.0;
    private double savings;
    private double finalAmount;
}