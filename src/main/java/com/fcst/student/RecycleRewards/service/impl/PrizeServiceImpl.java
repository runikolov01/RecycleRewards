package com.fcst.student.RecycleRewards.service.impl;

import com.fcst.student.RecycleRewards.model.Prize;
import com.fcst.student.RecycleRewards.repository.PrizeRepository;
import com.fcst.student.RecycleRewards.service.PrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrizeServiceImpl implements PrizeService {
    @Autowired
    private PrizeRepository prizeRepository;

    @Override
    public void savePrize(Prize prize) {
        prizeRepository.save(prize);
    }

    @Override
    public Prize getPrizeById(int id) {
        return null;
    }

    @Override
    public List<Prize> getAllPrizes() {
        return List.of();
    }

    @Override
    public void deletePrizeById(int id) {
    }
}