package com.fcst.student.RecycleRewards.model;

public class ReportDetails {
    private Long id;
    private String name;
    private int numberOfBottles;
    private Long topUserId;
    private int topUserBottles;

    public ReportDetails() {
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

    public int getNumberOfBottles() {
        return numberOfBottles;
    }

    public void setNumberOfBottles(int numberOfBottles) {
        this.numberOfBottles = numberOfBottles;
    }

    public Long getTopUserId() {
        return topUserId;
    }

    public void setTopUserId(Long topUserId) {
        this.topUserId = topUserId;
    }

    public int getTopUserBottles() {
        return topUserBottles;
    }

    public void setTopUserBottles(int topUserBottles) {
        this.topUserBottles = topUserBottles;
    }
}