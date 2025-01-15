package com.foodexpress.customer.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "customer_address")
public class CustomerAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int aid;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(nullable = false, length = 10)
    private String pincode;

    @Column(name = "house_apartment", nullable = false, length = 255)
    private String houseApartment;

    @Column(nullable = false, length = 255)
    private String area;

    @Column(length = 255)
    private String landmark;

    @Column(name = "towncity", nullable = false, length = 255)
    private String townCity;

    @Column(nullable = false, length = 255)
    private String state;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;
}
