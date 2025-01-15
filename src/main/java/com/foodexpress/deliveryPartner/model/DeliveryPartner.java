package com.foodexpress.deliveryPartner.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "delivery_partners")
public class DeliveryPartner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_partner_id")
    private Integer deliveryPartnerId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "serving_pincode", nullable = false, length = 20)
    private String servingPincode;

    @Column(name = "identity_proof", length = 255)
    private String identityProof;

    @Column(name = "phone_no", nullable = false, length = 15)
    private String phoneNo;

    @Column(name = "is_avail", nullable = false)
    private Boolean isAvail;

    @Column(name = "curr_loc_longitude", nullable = false, length = 255)
    private String currLocLongitude;

    @Column(name = "curr_loc_latitude", nullable = false, length = 255)
    private String currLocLatitude;
}
