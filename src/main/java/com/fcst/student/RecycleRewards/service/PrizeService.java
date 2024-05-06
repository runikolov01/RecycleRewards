package com.fcst.student.RecycleRewards.service;

import com.fcst.student.RecycleRewards.model.Prize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PrizeService {
    void savePrize(Prize prize);

    Prize getPrizeById(int id);

    List<Prize> getAllPrizes();

    void deletePrizeById(int id);
}
