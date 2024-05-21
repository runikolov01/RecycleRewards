package com.fcst.student.RecycleRewards.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private LocalDateTime issued;

    @Column(name = "active_until", nullable = false)
    private LocalDateTime activeUntil;

    @Column
    private Integer bottles;

    @Column(nullable = false)
    private Integer points;

    @Column(name = "registered_on")
    private LocalDateTime registeredOn;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private User registeredBy;

    @Column(name = "is_voucher", nullable = false)
    private Boolean isVoucher;


    public Ticket() {
    }

    public Ticket(String number) {
        this.number = number;
    }

    public Ticket(String number, LocalDateTime issued, LocalDateTime activeUntil, Integer bottles, Integer points, Boolean isVoucher) {
        this.number = number;
        this.issued = issued;
        this.activeUntil = activeUntil;
        this.bottles = bottles;
        this.points = points;
        this.isVoucher = isVoucher;
    }

    public Ticket(Long id, String number, LocalDateTime issued, LocalDateTime activeUntil, Integer bottles, Integer points, LocalDateTime registeredOn, User registeredBy) {
        this.id = id;
        this.number = number;
        this.issued = issued;
        this.activeUntil = activeUntil;
        this.bottles = bottles;
        this.points = points;
        this.registeredOn = registeredOn;
        this.registeredBy = registeredBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getIssued() {
        return issued;
    }

    public void setIssued(LocalDateTime issued) {
        this.issued = issued;
    }

    public LocalDateTime getActiveUntil() {
        return activeUntil;
    }

    public void setActiveUntil(LocalDateTime activeUntil) {
        this.activeUntil = activeUntil;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }


    public LocalDateTime getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(LocalDateTime registeredOn) {
        this.registeredOn = registeredOn;
    }

    public User getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(User registeredBy) {
        this.registeredBy = registeredBy;
    }

    public Integer getBottles() {
        return bottles;
    }

    public void setBottles(Integer bottles) {
        this.bottles = bottles;
    }

    public Boolean getVoucher() {
        return isVoucher;
    }

    public void setVoucher(Boolean voucher) {
        isVoucher = voucher;
    }
}