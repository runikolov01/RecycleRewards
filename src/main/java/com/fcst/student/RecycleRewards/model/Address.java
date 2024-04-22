package com.fcst.student.RecycleRewards.model;

import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String city;

    @Column
    private int postcode;

    @Column
    private String street;

    @Column
    private int number;

    @Column
    private int floor;

    @Column(name = "apartment_number")
    private int apartmentNumber;


    public Address() {
    }

    public Address(Long id, String city, int postcode, String street, int number, int floor, int apartmentNumber) {
        this.id = id;
        this.city = city;
        this.postcode = postcode;
        this.street = street;
        this.number = number;
        this.floor = floor;
        this.apartmentNumber = apartmentNumber;
    }

    public Address(Long id, String city, int postcode, String street, int number) {
        this.id = id;
        this.city = city;
        this.postcode = postcode;
        this.street = street;
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostcode() {
        return postcode;
    }

    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(int apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }
}