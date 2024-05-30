package com.fcst.student.RecycleRewards.service;

import com.fcst.student.RecycleRewards.model.PrizeDetailsDto;
import com.fcst.student.RecycleRewards.model.Purchase;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PurchaseService {
    List<Purchase> getPurchasesByUserId(Long userId);

    List<Purchase> getAllPurchasesByPrizeId(Long prizeId);

    Purchase getPurchaseById(Long id);

    String generateUniquePurchaseWinnerCode();

    List<PrizeDetailsDto> getPrizeDetailsForUser(Long userId);

    ResponseEntity<List<Purchase>> showUserPurchasedTickets(HttpServletRequest request);
}