package com.foodexpress.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
	private String restaurantName;
	private int menuItems;
    private int userCount;
    private int totalPrice;
    private int avgRating;
    private String location;
}
