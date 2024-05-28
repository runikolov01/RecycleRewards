package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.Prize;
import com.fcst.student.RecycleRewards.model.Purchase;
import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.model.enums.PrizeType;
import com.fcst.student.RecycleRewards.service.PrizeService;
import com.fcst.student.RecycleRewards.service.PurchaseService;
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
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class PrizeController {
    private final UserService userService;
    private final PrizeService prizeService;
    private final PurchaseService purchaseService;

    @Autowired
    public PrizeController(UserService userService, PrizeService prizeService, PurchaseService purchaseService) {
        this.userService = userService;
        this.prizeService = prizeService;
        this.purchaseService = purchaseService;
    }

    @GetMapping("/prizes")
    public String showPrizes(Model model, HttpSession session, @RequestParam(value = "type", required = false) PrizeType type) {
        setLoggedInAttribute(model, session, userService);

        List<Prize> prizes;
        if (type != null) {
            if (type.equals(PrizeType.INSTANT) || type.equals(PrizeType.RAFFLE)) {
                prizes = prizeService.getPrizesByTypeAndRemainedTicketsGreaterThanAndStartDateBefore(type, 0, LocalDateTime.now());
            } else {
                prizes = prizeService.getPrizesByTypeAndStartDateBefore(type, LocalDateTime.now());
            }
        } else {
            prizes = prizeService.getAllPrizesWithRemainedTicketsGreaterThanAndStartDateBefore(0, LocalDateTime.now());
        }


        session.setAttribute("prizes", prizes);
        model.addAttribute("prizes", prizes);

        if (type != null) {
            for (Prize prize : prizes) {
                List<User> participants = userService.getParticipantsByPrizeId(prize.getId());
                for (User participant : participants) {
                    model.addAttribute("participant", participant);
                }
            }
        }

        return "prizes";
    }

    public static void setLoggedInAttribute(Model model, HttpSession session, UserService userService) {
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
                String role = String.valueOf(user.getRole());
                if (role != null && role.equals("ADMIN")) {
                    session.setAttribute("loggedUser", user);
                    model.addAttribute("loggedUser", user);

                    Integer totalPoints = user.getTotalPoints();
                    session.setAttribute("totalPoints", totalPoints);
                    model.addAttribute("totalPoints", totalPoints);

                    List<Prize> prizesWithoutWinners = prizeService.getPrizesWithoutWinners();
                    model.addAttribute("prizes", prizesWithoutWinners);

                    if (prizeId != null) {
                        List<Purchase> purchases = purchaseService.getAllPurchasesByPrizeId(prizeId);
                        model.addAttribute("purchases", purchases);

                        List<User> participants = new ArrayList<>();
                        for (Purchase purchase : purchases) {
                            User purchaseUser = purchase.getUser();
                            participants.add(purchaseUser);
                        }
                        model.addAttribute("participants", participants);
                        model.addAttribute("selectedPrizeId", prizeId);

                        int remainingTickets = prizeService.getPrizeById(prizeId).get().getRemainedTickets();

                        model.addAttribute("remainingTickets", remainingTickets);

                    } else {
                        model.addAttribute("selectedPrizeId", null);
                    }

                    return "admin_raffle";
                } else {
                    return "redirect:/home";
                }
            }
        }

        return "redirect:/home";
    }

    @PostMapping("/draw_winner")
    @ResponseBody
    public ResponseEntity<String> drawWinner(@RequestParam Long prizeId, @RequestParam int randomNumber) {
        List<User> participants = userService.getParticipantsByPrizeId(prizeId);
        if (randomNumber > participants.size() || randomNumber <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Невалидно случайно число!");
        }

        User winner = participants.get(randomNumber - 1);

        prizeService.setWinnerForPrize(prizeId, winner.getId());

        return ResponseEntity.ok("Потребителят е свързан с наградата успешно!");
    }

    @PostMapping("/connectPrizeWithWinner")
    @ResponseBody
    public ResponseEntity<String> connectPrizeWithWinner(@RequestParam Long prizeId, @RequestParam Long purchaseId, @RequestParam Long userCode) {
        Prize prize = prizeService.getPrizeById(prizeId).orElse(null);
        User user = userService.getUserByUserCode(userCode);
        Purchase purchase = purchaseService.getPurchaseById(purchaseId);

        if (prize == null || user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Наградата не е намерена или потребителят не е намерен!");
        }

        List<Prize> userPrizes = user.getPrizes();
        if (userPrizes.contains(prize)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Потребителят вече е спечелил тази награда. Няма право на втора награда със същия уникален номер.");
        }

        user.getPrizes().add(prize);

        prize.setRemainedTickets(0);
        prize.setEndDate(LocalDateTime.now());

        String winnerCode = purchaseService.generateUniquePurchaseWinnerCode();

        purchase.setWinnerCode(winnerCode);

        userService.saveUser(user);

        return ResponseEntity.ok("Наградата е свързана с победителя успешно!");
    }


    @PostMapping("/prizes/buy")
    public ResponseEntity<String> buyPrize(@RequestParam Long prizeId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Integer totalPoints = (Integer) session.getAttribute("totalPoints");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Влезте в своя профил, за да продължите напред.");
        }

        Optional<Prize> optionalPrize = prizeService.getPrizeById(prizeId);
        if (optionalPrize.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Наградата не е намерена.");
        }

        Prize prize = optionalPrize.get();
        Integer neededPoints = prize.getNeededPointsToBuy();

        if (totalPoints < neededPoints) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Нямате достатъчно точки, за да закупите билет за тази награда.");
        }

        boolean purchaseSuccessful = prizeService.purchasePrize(userId, prizeId);

        if (purchaseSuccessful) {
            session.setAttribute("totalPoints", totalPoints - neededPoints);
            return ResponseEntity.ok("Билетът за тази награда е закупен успешно!");
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

        Long prizeCode = prizeService.generateUniquePrizeCode();
        prize.setPrizeCode(prizeCode);

        prizeService.savePrize(prize);

        return ResponseEntity.status(HttpStatus.CREATED).body("Вие добавихте наградата успешно!");
    }
}