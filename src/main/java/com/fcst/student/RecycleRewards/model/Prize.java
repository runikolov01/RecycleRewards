package com.fcst.student.RecycleRewards.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "prizes")
public class Prize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "points_to_buy", nullable = false)
    private int pointsToBuy;

    @Column(name = "total_tickets", nullable = false)
    private int totalTickets;

    @Column(name = "remained_tickets", nullable = false)
    private int remainedTickets;

    //TODO: Add the constructor, get & set methods

    //TODO: isActive, List<Participants>

    @OneToMany(mappedBy = "prize", cascade = CascadeType.ALL)
    private List<Winner> winners;
}
