package com.foodexpress.admin.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "restaurant_register_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Integer restaurantId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "phone_no", nullable = false, length = 15)
    private String phoneNo;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "close_time", nullable = false)
    private LocalTime closeTime;
    

    @Column(name = "location")
    private String location;

    @Column(name = "address")
    private String address;
}
