package com.example.padepokanshop.shop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(name = "customer_name", nullable = false)
    private String name;

    @Column(name = "customer_phone")
    private String phone;

    @Column(name = "customer_address")
    private String address;

    @Column(name = "customer_code", nullable = false)
    private String code;

    private String pic;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_order_date")
    private Date lastOrderDate;

    @Column(name = "is_active")
    private Boolean isActive;
}
