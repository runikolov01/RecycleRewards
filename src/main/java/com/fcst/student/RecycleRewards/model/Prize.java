package com.fcst.student.RecycleRewards.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "prizes")
public class Prize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "needed_points_to_buy", nullable = false)
    private int neededPointsToBuy;

    @Column(name = "total_tickets", nullable = false)
    private int totalTickets;

    @Column(name = "remained_tickets", nullable = false)
    private int remainedTickets;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @ManyToMany(mappedBy = "prizes")
    private List<Person> participants;

    public Prize() {

    }

    public Prize(Long id, String name, String description, int neededPointsToBuy, int totalTickets, int remainedTickets, LocalDateTime startDate, LocalDateTime endDate, List<Person> participants) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.neededPointsToBuy = neededPointsToBuy;
        this.totalTickets = totalTickets;
        this.remainedTickets = remainedTickets;
        this.startDate = startDate;
        this.endDate = endDate;
        this.participants = participants;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNeededPointsToBuy() {
        return neededPointsToBuy;
    }

    public void setNeededPointsToBuy(int neededPointsToBuy) {
        this.neededPointsToBuy = neededPointsToBuy;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getRemainedTickets() {
        return remainedTickets;
    }

    public void setRemainedTickets(int remainedTickets) {
        this.remainedTickets = remainedTickets;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public List<Person> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Person> participants) {
        this.participants = participants;
    }
}
