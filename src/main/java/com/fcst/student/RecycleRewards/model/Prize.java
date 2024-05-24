package com.fcst.student.RecycleRewards.model;

import com.fcst.student.RecycleRewards.model.enums.PrizeType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "prizes")
public class Prize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, name = "prize_code")
    private Long prizeCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "needed_points_to_buy", nullable = false)
    private Integer neededPointsToBuy;

    @Column(name = "total_tickets", nullable = false)
    private Integer totalTickets;

    @Column(name = "remained_tickets")
    private Integer remainedTickets;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;


    @ManyToMany(mappedBy = "prizes")
    private List<User> participants;

    @Enumerated(EnumType.STRING)
    private PrizeType type;


    public Prize() {

    }

    public Prize(Long id, String name, String description, Integer neededPointsToBuy, Integer totalTickets, Integer remainedTickets, LocalDateTime startDate, List<User> participants, PrizeType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.neededPointsToBuy = neededPointsToBuy;
        this.totalTickets = totalTickets;
        this.remainedTickets = remainedTickets;
        this.startDate = startDate;
        this.participants = participants;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPrizeCode() {
        return prizeCode;
    }

    public void setPrizeCode(Long prizeCode) {
        this.prizeCode = prizeCode;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
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

    public Integer getNeededPointsToBuy() {
        return neededPointsToBuy;
    }

    public void setNeededPointsToBuy(Integer neededPointsToBuy) {
        this.neededPointsToBuy = neededPointsToBuy;
    }

    public Integer getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(Integer totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getRemainedTickets() {
        return remainedTickets;
    }

    public void setRemainedTickets(Integer remainedTickets) {
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

    public List<User> getParticipants() {
        return participants;
    }

    public void addParticipant(User user) {
        participants.add(user);
    }

    public void removeParticipant(User user) {
        participants.remove(user);
    }


    public PrizeType getType() {
        return type;
    }

    public void setType(PrizeType type) {
        this.type = type;
    }
}