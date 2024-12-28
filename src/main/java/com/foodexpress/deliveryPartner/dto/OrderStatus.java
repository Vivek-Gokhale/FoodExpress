package com.foodexpress.deliveryPartner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatus {
	private Integer deliveryId;
	private Integer orderId;      // Order ID
    private Integer userId;       // User ID
    private String userName;      // User Name
    private String phoneNo;       // Phone Number
    private String address;       // Address
    private Integer totalAmount;   // Total Amount
    private String paymentOption; // Payment Option
    ;  
}
