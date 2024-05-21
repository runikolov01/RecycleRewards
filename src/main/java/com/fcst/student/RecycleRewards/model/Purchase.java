package com.fcst.student.RecycleRewards.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "prize_id", nullable = false)
    private Prize prize;

    private LocalDateTime purchaseDate;

    @Column(name = "winner_code")
    private String winnerCode;

    public Purchase(Long id, User user, Prize prize, LocalDateTime purchaseDate, String winnerCode) {
        this.id = id;
        this.user = user;
        this.prize = prize;
        this.purchaseDate = purchaseDate;
        this.winnerCode = winnerCode;
    }

    public Purchase() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Prize getPrize() {
        return prize;
    }

    public void setPrize(Prize prize) {
        this.prize = prize;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getWinnerCode() {
        return winnerCode;
    }

    public void setWinnerCode(String winnerCode) {
        this.winnerCode = winnerCode;
    }
}
