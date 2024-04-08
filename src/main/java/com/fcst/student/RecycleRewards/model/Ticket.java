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

    @Column(nullable = false)
    private Integer points;

    @Column(name = "id_user")
    private Integer idUser;

    @Column(name = "registered_on")
    private LocalDateTime registeredOn;

    public Ticket() {
    }

    public Ticket(String number) {
        this.number = number;
    }

    public Ticket(String number, LocalDateTime issued, LocalDateTime activeUntil, Integer points) {
        this.number = number;
        this.issued = issued;
        this.activeUntil = activeUntil;
        this.points = points;
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

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public LocalDateTime getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(LocalDateTime registeredOn) {
        this.registeredOn = registeredOn;
    }
}