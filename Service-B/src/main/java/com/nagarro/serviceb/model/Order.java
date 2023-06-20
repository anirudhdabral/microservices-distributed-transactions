package com.nagarro.serviceb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_details")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private int orderId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "product_id")
    private int productId;

    @Column(name = "order_quantity")
    private int orderQuantity;

    @Column(name = "order_status")
    private String orderStatus = "pending";
}
