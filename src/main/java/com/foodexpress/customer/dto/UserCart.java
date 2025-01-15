package com.foodexpress.customer.dto;

import lombok.Data;

@Data
public class UserCart {
    private Integer cartItemId;
    private Integer userId;
    private Integer itemId;
    private Integer quantity;
    private Integer price;
    private String itemName;
    private String image;
}
