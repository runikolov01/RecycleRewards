package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.Purchase;
import com.fcst.student.RecycleRewards.service.PurchaseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PurchaseController {
    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping("/userPurchasedTickets")
    public ResponseEntity<List<Purchase>> getUserPurchasedTickets(HttpServletRequest request) {
        return purchaseService.showUserPurchasedTickets(request);
    }
}