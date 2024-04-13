package com.fcst.student.RecycleRewards.model;

import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String city;


    @Column(nullable = false)
    private int postcode;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private int number;

    @Column(nullable = true)
    private int apartmentNumber;

    @Column(nullable = true)
    private int floor;

    //TODO: Add the constructor, get & set methods
}