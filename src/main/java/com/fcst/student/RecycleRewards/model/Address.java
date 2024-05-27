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

    @Column(name = "postcode")
    private int postcode;

    @Column
    private String street;

    @Column(name = "street_number")
    private Integer streetNumber;

    @Column
    private Integer floor;

    @Column(name = "apartment_number")
    private Integer apartmentNumber;


    public Address() {
    }

    public Address(Long id, String city, int postcode, String street, Integer streetNumber, Integer floor, Integer apartmentNumber) {
        this.id = id;
        this.city = city;
        this.postcode = postcode;
        this.street = street;
        this.streetNumber = streetNumber;
        this.floor = floor;
        this.apartmentNumber = apartmentNumber;
    }

    public Address(Long id, String city, int postcode, String street, Integer streetNumber) {
        this.id = id;
        this.city = city;
        this.postcode = postcode;
        this.street = street;
        this.streetNumber = streetNumber;
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

    public Integer getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Integer getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(Integer apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public void setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }
}