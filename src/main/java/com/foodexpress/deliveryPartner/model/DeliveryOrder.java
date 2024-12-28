package com.foodexpress.deliveryPartner.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "deliveries")
public class DeliveryOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Integer deliveryId;

 
    @Column(name = "delivery_partner_id", nullable = false)
    private Integer deliveryPartnerId;

    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    @Column(name = "status", nullable = false, length = 100)
    private String status;
    
    private static DeliveryOrder instance;
    
    public static synchronized DeliveryOrder getInstance() {
        if (instance == null) {
            instance = new DeliveryOrder();
        }
        return instance;
    }
}
