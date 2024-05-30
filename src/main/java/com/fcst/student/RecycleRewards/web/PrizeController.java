package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.enums.PrizeType;
import com.fcst.student.RecycleRewards.service.PrizeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PrizeController {
    @Autowired
    PrizeService prizeService;

    @GetMapping("/prizes")
    public String showPrizes(Model model, HttpSession session, @RequestParam(value = "type", required = false) PrizeType type) {
        return prizeService.openPrizesPage(model, session, type);
    }

    @GetMapping("/admin_add_prizes")
    public String showAddPrizesPage(Model model, HttpSession session) {
        return prizeService.openAddPrizesPage(model, session);
    }

    @GetMapping("/admin_raffle")
    public String showRafflePage(Model model, HttpSession session, @RequestParam(value = "prizeId", required = false) Long prizeId) {
        return prizeService.openRafflePage(model, session, prizeId);
    }

    @PostMapping("/draw_winner")
    @ResponseBody
    public ResponseEntity<String> drawWinner(@RequestParam Long prizeId, @RequestParam int randomNumber) {
        return prizeService.chooseWinner(prizeId, randomNumber);
    }

    @PostMapping("/connectPrizeWithWinner")
    @ResponseBody
    public ResponseEntity<String> connectPrizeWithWinner(@RequestParam Long prizeId, @RequestParam Long purchaseId, @RequestParam Long userCode) {
        return prizeService.setPrizeForWinner(prizeId, purchaseId, userCode);
    }

    @PostMapping("/prizes/buy")
    public ResponseEntity<String> buyPrize(@RequestParam Long prizeId, HttpSession session) {
        return prizeService.takePrize(prizeId, session);
    }

    @PostMapping("/admin_add_prizes")
    public ResponseEntity<String> savePrize(@RequestParam String name, @RequestParam String description, @RequestParam Integer neededPointsToBuy, @RequestParam Integer totalTickets, @RequestParam String startDateTime, @RequestParam PrizeType type) {
        return prizeService.addPrizeToDB(name, description, neededPointsToBuy, totalTickets, startDateTime, type);
    }
}