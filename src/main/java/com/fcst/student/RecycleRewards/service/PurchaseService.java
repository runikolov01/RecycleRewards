package com.fcst.student.RecycleRewards.service;

import com.fcst.student.RecycleRewards.model.Purchase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PurchaseService {
    List<Purchase> getUserPurchasedTickets(Long userId);

    List<Purchase> getPurchasesByUserId(Long userId);

    List<Purchase> getAllPurchasesByPrizeId(Long prizeId);
    Purchase getPurchaseById(Long id);

}
