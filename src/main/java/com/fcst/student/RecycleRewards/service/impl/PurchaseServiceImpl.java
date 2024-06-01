package com.fcst.student.RecycleRewards.service.impl;

import com.fcst.student.RecycleRewards.model.PrizeDetailsDto;
import com.fcst.student.RecycleRewards.model.Purchase;
import com.fcst.student.RecycleRewards.repository.PurchaseRepository;
import com.fcst.student.RecycleRewards.service.PurchaseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private static final int UNIQUE_CODE_LENGTH = 8;
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
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
    @Transactional
    public String generateUniquePurchaseWinnerCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder ticketNumberBuilder = new StringBuilder(UNIQUE_CODE_LENGTH);

        while (true) {
            for (int i = 0; i < UNIQUE_CODE_LENGTH; i++) {
                ticketNumberBuilder.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
            }

            String purchaseWinnerCode = ticketNumberBuilder.toString();

            if (!purchaseRepository.existsByWinnerCode(purchaseWinnerCode)) {
                return purchaseWinnerCode;
            }
            ticketNumberBuilder.setLength(0);
        }
    }

    @Override
    public List<PrizeDetailsDto> getPrizeDetailsForUser(Long userId) {
        return purchaseRepository.findPrizeDetailsByUserId(userId);
    }

    @Override
    public ResponseEntity<List<Purchase>> showUserPurchasedTickets(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Purchase> purchasedTickets = getPurchasesByUserId(userId);
        return ResponseEntity.ok(purchasedTickets);
    }
}