package com.fcst.student.RecycleRewards.service;

import com.fcst.student.RecycleRewards.model.Prize;
import com.fcst.student.RecycleRewards.model.PrizeDetailsDto;
import com.fcst.student.RecycleRewards.model.enums.PrizeType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public interface PrizeService {
    void savePrize(Prize prize);

    Optional<Prize> getPrizeById(Long id);

    List<Prize> getAllPrizes();

    boolean purchasePrize(Long userId, Long prizeId);

    void setWinnerForPrize(Long prizeId, Long userId);

    List<Prize> getPrizesWithoutWinners();

    List<Prize> getPrizesByTypeAndStartDateBefore(PrizeType type, LocalDateTime date);

    List<Prize> getAllPrizesWithRemainedTicketsGreaterThanAndStartDateBefore(Integer tickets, LocalDateTime date);

    List<Prize> getPrizesByTypeAndRemainedTicketsGreaterThanAndStartDateBefore(PrizeType type, Integer tickets, LocalDateTime date);

    List<PrizeDetailsDto> getPrizeDetailsByUserId(Long userId);

}
