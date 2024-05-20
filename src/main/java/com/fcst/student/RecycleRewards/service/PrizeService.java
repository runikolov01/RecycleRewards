package com.fcst.student.RecycleRewards.service;

import com.fcst.student.RecycleRewards.model.Prize;
import com.fcst.student.RecycleRewards.model.enums.PrizeType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PrizeService {
    void savePrize(Prize prize);

    Optional<Prize> getPrizeById(Long id);

    List<Prize> getAllPrizes();

    List<Prize> getPrizesByType(PrizeType type);

    void deletePrizeById(int id);

    boolean purchasePrize(Long userId, Long prizeId);

    void setWinnerForPrize(Long prizeId, Long userId);

    public List<Prize> getPrizesWithoutWinners();
}
