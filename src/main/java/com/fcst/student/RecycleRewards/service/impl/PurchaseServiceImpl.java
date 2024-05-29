package com.fcst.student.RecycleRewards.service.impl;

import com.fcst.student.RecycleRewards.model.PrizeDetailsDto;
import com.fcst.student.RecycleRewards.model.Purchase;
import com.fcst.student.RecycleRewards.repository.PurchaseRepository;
import com.fcst.student.RecycleRewards.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

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

    @Override
    public String generateUniquePurchaseWinnerCode() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder ticketNumberBuilder = new StringBuilder();
        Random random = new Random();
        int length = 8;

        while (true) {
            for (int i = 0; i < length; i++) {
                ticketNumberBuilder.append(alphabet.charAt(random.nextInt(alphabet.length())));
            }

            String purchaseWinnerCode = ticketNumberBuilder.toString();

            if (!purchaseWinnerCodeExistsInDatabase(purchaseWinnerCode)) {
                return purchaseWinnerCode;
            }
            ticketNumberBuilder.setLength(0);
        }
    }

//    @Override
//    public List<String> getCodes(Long userId, Long prizeId) {
//        return purchaseRepository.findCodesByUserIdAndPrizeId(userId, prizeId);
//    }

    @Override
    public List<PrizeDetailsDto> getPrizeDetailsForUser(Long userId) {
        return purchaseRepository.findPrizeDetailsByUserId(userId);
    }

    private boolean purchaseWinnerCodeExistsInDatabase(String winnerCode) {
        return purchaseRepository.existsByWinnerCode(winnerCode);
    }
}
