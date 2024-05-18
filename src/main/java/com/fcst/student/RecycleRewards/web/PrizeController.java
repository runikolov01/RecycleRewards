package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.Prize;
import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.model.enums.PrizeType;
import com.fcst.student.RecycleRewards.service.PrizeService;
import com.fcst.student.RecycleRewards.service.UserService;
import jakarta.servlet.http.HttpSession;
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
import java.util.Optional;

@Controller
public class PrizeController {
    private final UserService userService;
    private final PrizeService prizeService;

    @Autowired
    public PrizeController(UserService userService, PrizeService prizeService) {
        this.userService = userService;
        this.prizeService = prizeService;
    }

    @GetMapping("/prizes")
    public String showPrizes(Model model, HttpSession session, @RequestParam(value = "type", required = false) PrizeType type) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            User user = userService.getUserById(userId);
            if (user != null) {
                model.addAttribute("loggedUser", user);
                Integer totalPoints = user.getTotalPoints();
                model.addAttribute("totalPoints", totalPoints);
            }
        }
        List<Prize> prizes;
        if (type != null) {
            prizes = prizeService.getPrizesByType(type);
        } else {
            prizes = prizeService.getAllPrizes();
        }
        model.addAttribute("prizes", prizes);
        return "prizes";
    }

    @GetMapping("/admin_add_prizes")
    public String openAddPrizesPage(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            User user = userService.getUserById(userId);
            if (user != null) {
                String role = String.valueOf(user.getRole());
                if (role != null && role.equals("ADMIN")) {
                    Integer totalPoints = user.getTotalPoints();
                    model.addAttribute("totalPoints", totalPoints);
                    model.addAttribute("loggedUser", user);
                    model.addAttribute("loggedIn", true);
                    model.addAttribute("loggedUser", user);
                    model.addAttribute("prizeTypes", PrizeType.values());

                    return "admin_add_prizes";
                } else {
                    System.out.println("User role: " + role);
                }
            } else {
                System.out.println("User is null for userId: " + userId);
            }
        }
        List<Prize> prizes = prizeService.getAllPrizes();
        model.addAttribute("prizes", prizes);
        return "redirect:/home";
    }

    @PostMapping("/prizes")
    public ResponseEntity<String> buyPrize() {
        return ResponseEntity.status(HttpStatus.CREATED).body("Вие купихте билет успешно!");

    }

    @PostMapping("/prizes/buy")
    public ResponseEntity<String> buyPrize(@RequestParam Long prizeId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need to log in to buy a ticket for this prize");
        }

        Optional<Prize> optionalPrize = prizeService.getPrizeById(prizeId);
        if (optionalPrize.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prize not found");
        }

        Prize prize = optionalPrize.get();
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (user.getTotalPoints() < prize.getNeededPointsToBuy()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You don't have enough points to buy a ticket for this prize");
        }

        // Deduct points from user's total points
        user.setTotalPoints(user.getTotalPoints() - prize.getNeededPointsToBuy());

        // Deduct 1 from remaining tickets for the prize
        prize.setRemainedTickets(prize.getRemainedTickets() - 1);

        // Add user as a participant to the prize
        prize.getParticipants().add(user);

        // Save updated entities
        userService.saveUser(user);
        prizeService.savePrize(prize);

        return ResponseEntity.status(HttpStatus.CREATED).body("Ticket purchased successfully!");
    }



    @PostMapping("/admin_add_prizes")
    public ResponseEntity<String> savePrize(@RequestParam String name,
                                            @RequestParam String description,
                                            @RequestParam Integer neededPointsToBuy,
                                            @RequestParam Integer totalTickets,
                                            @RequestParam String startDateTime,
                                            @RequestParam String endDateTime,
                                            @RequestParam PrizeType type) {

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
        prize.setType(type);

        prizeService.savePrize(prize);

        return ResponseEntity.status(HttpStatus.CREATED).body("Вие добавихте наградата успешно!");
    }
}