package com.foodexpress.deliveryPartner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendingOrderStatus {
    private Integer orderId;
    private Integer userId;
    private String address;
}
