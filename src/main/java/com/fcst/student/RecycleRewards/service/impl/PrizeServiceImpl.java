package com.fcst.student.RecycleRewards.service.impl;

import com.fcst.student.RecycleRewards.model.Prize;
import com.fcst.student.RecycleRewards.model.enums.PrizeType;
import com.fcst.student.RecycleRewards.repository.PrizeRepository;
import com.fcst.student.RecycleRewards.service.PrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrizeServiceImpl implements PrizeService {
    @Autowired
    private final PrizeRepository prizeRepository;

    public PrizeServiceImpl(PrizeRepository prizeRepository) {
        this.prizeRepository = prizeRepository;
    }

    @Override
    public void savePrize(Prize prize) {
        prizeRepository.save(prize);
    }

    @Override
    public Optional<Prize> getPrizeById(Long id) {
        return prizeRepository.findById(id);
    }

    @Override
    public List<Prize> getAllPrizes() {
        return prizeRepository.findAll();
    }

    @Override
    public List<Prize> getPrizesByType(PrizeType type) {
        return prizeRepository.findByType(type);
    }

    @Override
    public void deletePrizeById(int id) {
    }
}