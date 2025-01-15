package com.foodexpress.admin.dto;

import java.sql.Date;
import java.sql.Time;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodItems {
    private Integer restaurantId;
    private String restaurantName;
    private String email;
    private String phoneNo;
    private Date registrationDate;
    private Time startTime;
    private Time closeTime;
    private String address;
    private String location;
    private Integer itemId;
    private String itemName;
    private Integer price;
    private Integer stock;
    private String description;
    private String images;
    private double rating;
}
