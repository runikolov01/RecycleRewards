package com.fcst.student.RecycleRewards.model;

import java.time.LocalDateTime;

public class PrizeDetailsDto {

    private Long prizeId;
    private String prizeName;
    private String prizeDescription;
    private LocalDateTime prizeDate;
    private String winnerCode;

    public PrizeDetailsDto(Long prizeId, String prizeName, String prizeDescription, LocalDateTime prizeDate, String winnerCode) {
        this.prizeId = prizeId;
        this.prizeName = prizeName;
        this.prizeDescription = prizeDescription;
        this.prizeDate = prizeDate;
        this.winnerCode = winnerCode;
    }

    public PrizeDetailsDto(Long prizeId, String prizeName, String prizeDescription, String winnerCode) {
        this.prizeId = prizeId;
        this.prizeName = prizeName;
        this.prizeDescription = prizeDescription;
        this.winnerCode = winnerCode;
    }

    public PrizeDetailsDto() {
    }

    public Long getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(Long prizeId) {
        this.prizeId = prizeId;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getPrizeDescription() {
        return prizeDescription;
    }

    public void setPrizeDescription(String prizeDescription) {
        this.prizeDescription = prizeDescription;
    }

    public LocalDateTime getPrizeDate() {
        return prizeDate;
    }

    public void setPrizeDate(LocalDateTime prizeDate) {
        this.prizeDate = prizeDate;
    }

    public String getWinnerCode() {
        return winnerCode;
    }

    public void setWinnerCode(String winnerCode) {
        this.winnerCode = winnerCode;
    }
}
