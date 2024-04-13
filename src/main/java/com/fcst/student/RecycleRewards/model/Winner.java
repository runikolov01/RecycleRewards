package com.fcst.student.RecycleRewards.model;

import jakarta.persistence.*;

@Entity
@Table(name = "winners")
public class Winner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "person_id", nullable = false)
    private int personId;

    @Column(name = "prize_id", nullable = false)
    private int prizeId;

    @ManyToOne
    @JoinColumn(name = "prize_id", nullable = false)
    private Prize prize;

    //TODO: Add the constructor, get & set methods
}
