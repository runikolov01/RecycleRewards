package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.Purchase;
import com.fcst.student.RecycleRewards.service.PurchaseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;


    @GetMapping("/userPurchasedTickets")
    public ResponseEntity<List<Purchase>> getUserPurchasedTickets(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId"); // Get user ID from session
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Purchase> purchasedTickets = purchaseService.getUserPurchasedTickets(userId);

        return ResponseEntity.ok(purchasedTickets);
    }
}
