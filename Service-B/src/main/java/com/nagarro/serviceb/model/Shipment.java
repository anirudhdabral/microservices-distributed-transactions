package com.nagarro.serviceb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "shipmentId")
    private int shipmentId;

    @Column(name = "order_id")
    private int orderId;

    @Column(name = "city")
    private String city;

    @Column(name = "shipment_status")
    private String shipmentStatus="not_created";
}
