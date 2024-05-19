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
        Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
        if (loggedIn == null) {
            loggedIn = false;
        }
        session.setAttribute("loggedIn", loggedIn);
        model.addAttribute("loggedIn", loggedIn);

        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            User user = userService.getUserById(userId);
            if (user != null) {
                session.setAttribute("loggedUser", user);
                model.addAttribute("loggedUser", user);

                Integer totalPoints = user.getTotalPoints();
                session.setAttribute("totalPoints", totalPoints);
                model.addAttribute("totalPoints", totalPoints);
            }
        }

        List<Prize> prizes;
        if (type != null) {
            prizes = prizeService.getPrizesByType(type);
        } else {
            prizes = prizeService.getAllPrizes();
        }
        session.setAttribute("prizes", prizes);
        model.addAttribute("prizes", prizes);

        // If type is specified, fetch participants for each prize
        if (type != null) {
            for (Prize prize : prizes) {
                List<User> participants = userService.getParticipantsByPrizeId(prize.getId());
                // Add participants to the model multiple times based on the number of purchases
                for (User participant : participants) {
                    model.addAttribute("participant", participant);
                }
            }
        }

        return "prizes";
    }


    @GetMapping("/admin_add_prizes")
    public String openAddPrizesPage(Model model, HttpSession session) {
        Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
        if (loggedIn == null) {
            loggedIn = false;
        }
        session.setAttribute("loggedIn", loggedIn);
        model.addAttribute("loggedIn", loggedIn);

        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            User user = userService.getUserById(userId);
            if (user != null) {
                String role = String.valueOf(user.getRole());
                if (role != null && role.equals("ADMIN")) {
                    Integer totalPoints = user.getTotalPoints();
                    session.setAttribute("loggedUser", user);
                    session.setAttribute("totalPoints", totalPoints);

                    model.addAttribute("totalPoints", totalPoints);
                    model.addAttribute("loggedUser", user);
                    model.addAttribute("loggedIn", true);
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

    @GetMapping("/admin_raffle")
    public String openRafflePage(Model model, HttpSession session, @RequestParam(value = "prizeId", required = false) Long prizeId) {
        Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
        if (loggedIn == null) {
            loggedIn = false;
        }
        session.setAttribute("loggedIn", loggedIn);
        model.addAttribute("loggedIn", loggedIn);

        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            User user = userService.getUserById(userId);
            if (user != null) {
                session.setAttribute("loggedUser", user);
                model.addAttribute("loggedUser", user);

                Integer totalPoints = user.getTotalPoints();
                session.setAttribute("totalPoints", totalPoints);
                model.addAttribute("totalPoints", totalPoints);
            }
        }

        List<Prize> prizes = prizeService.getPrizesByType(PrizeType.RAFFLE);
        model.addAttribute("prizes", prizes);

        if (prizeId != null) {
            List<User> participants = userService.getParticipantsByPrizeId(prizeId);
            model.addAttribute("participants", participants);
        }

        return "admin_raffle";
    }

//    @PostMapping("/draw_winner")
//    @ResponseBody
//    public ResponseEntity<User> drawWinner(@RequestParam Long prizeId, @RequestParam int randomNumber) {
//        List<User> participants = userService.getParticipantsByPrizeId(prizeId);
//        if (participants.size() < randomNumber) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//        User winner = participants.get(randomNumber - 1); // Assuming the participants are 0-indexed
//
//        // Update the winner information in the database
//        prizeService.setWinnerForPrize(prizeId, winner.getId());
//
//        return ResponseEntity.ok(winner);
//    }


    @PostMapping("/prizes/buy")
    public ResponseEntity<String> buyPrize(@RequestParam Long prizeId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Integer totalPoints = (Integer) session.getAttribute("totalPoints");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Влезте в своя профил, за да продължите напред.");
        }

        Optional<Prize> optionalPrize = prizeService.getPrizeById(prizeId);
        if (!optionalPrize.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Наградата не е намерена.");
        }

        Prize prize = optionalPrize.get();
        Integer neededPoints = prize.getNeededPointsToBuy();

        if (totalPoints < neededPoints) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Нямате достатъчно точки, за да купите билет за тази награда.");
        }

        boolean purchaseSuccessful = prizeService.purchasePrize(userId, prizeId);

        if (purchaseSuccessful) {
            session.setAttribute("totalPoints", totalPoints - neededPoints);
            return ResponseEntity.ok("Билетът за тази награда е купен успешно!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Възникна грешка, моля опитайте отново!");
        }
    }


    @PostMapping("/admin_add_prizes")
    public ResponseEntity<String> savePrize(@RequestParam String name, @RequestParam String description, @RequestParam Integer neededPointsToBuy, @RequestParam Integer totalTickets, @RequestParam String startDateTime, @RequestParam PrizeType type) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        LocalDateTime startDate = LocalDateTime.parse(startDateTime, formatter);

        Prize prize = new Prize();
        prize.setName(name);
        prize.setDescription(description);
        prize.setNeededPointsToBuy(neededPointsToBuy);
        prize.setTotalTickets(totalTickets);
        prize.setStartDate(startDate);
        prize.setRemainedTickets(totalTickets);
        prize.setType(type);

        prizeService.savePrize(prize);

        return ResponseEntity.status(HttpStatus.CREATED).body("Вие добавихте наградата успешно!");
    }
}