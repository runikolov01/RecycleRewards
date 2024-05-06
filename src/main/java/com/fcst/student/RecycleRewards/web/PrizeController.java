package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.Prize;
import com.fcst.student.RecycleRewards.service.PrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class PrizeController {

    private final PrizeService prizeService;

    @Autowired
    public PrizeController(PrizeService prizeService) {
        this.prizeService = prizeService;
    }

    @GetMapping("/prizes")
    public String showPrizes(Model model) {
        List<Prize> prizes = prizeService.getAllPrizes();
        model.addAttribute("prizes", prizes);
        return "prizes";
    }

    @GetMapping("/admin_add_prizes")
    public String openAddPrizesPage() {
        return "admin_add_prizes";
    }

    @PostMapping("/admin_add_prizes")
    public ResponseEntity<String> savePrize(@RequestParam String name,
                                            @RequestParam String description,
                                            @RequestParam Integer neededPointsToBuy,
                                            @RequestParam Integer totalTickets,
                                            @RequestParam String startDateTime,
                                            @RequestParam String endDateTime) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        LocalDateTime startDate = LocalDateTime.parse(startDateTime, formatter);
        LocalDateTime endDate = LocalDateTime.parse(endDateTime, formatter);

        Prize prize = new Prize();
        prize.setName(name);
        prize.setDescription(description);
        prize.setNeededPointsToBuy(neededPointsToBuy);
        prize.setTotalTickets(totalTickets);
        prize.setStartDate(startDate);
        prize.setEndDate(endDate);
        prize.setRemainedTickets(totalTickets);

        prizeService.savePrize(prize);

        return ResponseEntity.status(HttpStatus.CREATED).body("Вие добавихте наградата успешно!");
    }
}