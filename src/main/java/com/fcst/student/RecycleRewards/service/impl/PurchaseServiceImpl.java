package com.fcst.student.RecycleRewards.service.impl;

import com.fcst.student.RecycleRewards.model.Purchase;
import com.fcst.student.RecycleRewards.repository.PurchaseRepository;
import com.fcst.student.RecycleRewards.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public List<Purchase> getUserPurchasedTickets(Long userId) {
        return purchaseRepository.findByUserId(userId);
    }

    @Override
    public List<Purchase> getPurchasesByUserId(Long userId) {
        return purchaseRepository.findByUserId(userId);
    }

    @Override
    public List<Purchase> getAllPurchasesByPrizeId(Long prizeId) {
        return purchaseRepository.findAllByPrizeId(prizeId);
    }

    @Override
    public Purchase getPurchaseById(Long id) {
        return purchaseRepository.findById(id).orElse(null);
    }


}
