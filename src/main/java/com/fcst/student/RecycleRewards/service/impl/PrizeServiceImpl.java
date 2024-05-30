package com.fcst.student.RecycleRewards.service.impl;

import com.fcst.student.RecycleRewards.model.Prize;
import com.fcst.student.RecycleRewards.model.Purchase;
import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.model.enums.PrizeType;
import com.fcst.student.RecycleRewards.repository.PrizeRepository;
import com.fcst.student.RecycleRewards.repository.PurchaseRepository;
import com.fcst.student.RecycleRewards.repository.UserRepository;
import com.fcst.student.RecycleRewards.service.PrizeService;
import com.fcst.student.RecycleRewards.service.PurchaseService;
import com.fcst.student.RecycleRewards.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class PrizeServiceImpl implements PrizeService {
    private final UserService userService;
    private final PurchaseService purchaseService;
    private final PrizeRepository prizeRepository;
    private final UserRepository userRepository;
    private final PurchaseRepository purchaseRepository;

    @Autowired
    public PrizeServiceImpl(PrizeRepository prizeRepository, UserService userService, PurchaseService purchaseService, UserRepository userRepository, PurchaseRepository purchaseRepository) {
        this.userService = userService;
        this.purchaseService = purchaseService;
        this.prizeRepository = prizeRepository;
        this.userRepository = userRepository;
        this.purchaseRepository = purchaseRepository;
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
            Optional<User> user = userService.getUserById(userId);
            if (user.isPresent()) {
                session.setAttribute("loggedUser", user);
                model.addAttribute("loggedUser", user);

                Integer totalPoints = user.get().getTotalPoints();
                session.setAttribute("totalPoints", totalPoints);
                model.addAttribute("totalPoints", totalPoints);
            }
        }
    }

    @Transactional
    @Override
    public void savePrize(Prize prize) {
        prizeRepository.save(prize);
    }

    @Override
    public Optional<Prize> getPrizeById(Long id) {
        return prizeRepository.findById(id);
    }

    @Override
    public List<Prize> getAllPrizes() {
        return prizeRepository.findAll();
    }

    @Transactional
    @Override
    public boolean purchasePrize(Long userId, Long prizeId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Prize> prizeOpt = prizeRepository.findById(prizeId);

        if (userOpt.isEmpty() || prizeOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        Prize prize = prizeOpt.get();
        int remainedTicketsForThisPrize = prize.getRemainedTickets();

        if (user.getTotalPoints() < prize.getNeededPointsToBuy() || remainedTicketsForThisPrize <= 0) {
            return false;
        }

        user.setTotalPoints(user.getTotalPoints() - prize.getNeededPointsToBuy());
        prize.setRemainedTickets(remainedTicketsForThisPrize - 1);
        userRepository.save(user);

        Purchase purchase = new Purchase();

        if (prize.getType() == PrizeType.INSTANT) {
            String prizeWonCode = purchaseService.generateUniquePurchaseWinnerCode();
            purchase.setWinnerCode(prizeWonCode);
            user.getPrizes().add(prize);
            if (prize.getRemainedTickets() == 0) {
                prize.setEndDate(LocalDateTime.now());
            }
        }

        purchase.setUser(user);
        purchase.setPrize(prize);
        purchase.setPurchaseDate(LocalDateTime.now());
        purchaseRepository.save(purchase);

        return true;
    }

    @Transactional
    @Override
    public void setWinnerForPrize(Long prizeId, Long userId) {
        Optional<Prize> optionalPrize = prizeRepository.findById(prizeId);
        Optional<User> optionalWinner = userRepository.findById(userId);
        if (optionalPrize.isPresent() && optionalWinner.isPresent()) {
            Prize prize = optionalPrize.get();
            User winner = optionalWinner.get();
            prize.addParticipant(winner);
            prize.setRemainedTickets(0);
            prize.setEndDate(LocalDateTime.now());
            prizeRepository.save(prize);
        }
    }

    @Override
    public List<Prize> getPrizesWithoutWinners() {
        return prizeRepository.findRafflePrizesWithoutWinners();
    }

    @Override
    public List<Prize> getAllPrizesWithRemainedTicketsGreaterThanAndStartDateBefore(Integer tickets, LocalDateTime
            date) {
        return prizeRepository.findByRemainedTicketsGreaterThanAndStartDateBefore(tickets, date);
    }

    @Override
    public List<Prize> getPrizesByTypeAndRemainedTicketsGreaterThanAndStartDateBefore(PrizeType type, Integer
            tickets, LocalDateTime date) {
        return prizeRepository.findByTypeAndRemainedTicketsGreaterThanAndStartDateBefore(type, tickets, date);
    }

    @Override
    public Long generateUniquePrizeCode() {
        Long prizeCode;
        do {
            prizeCode = generateRandomCode();
        } while (userRepository.existsByUserCode(prizeCode));
        return prizeCode;
    }

    @Override
    public Long generateRandomCode() {
        Random random = new Random();
        return 10000000L + Math.abs(random.nextLong() % 90000000L);
    }

    @Override
    public List<Prize> getPrizesByTypeAndStartDateBefore(PrizeType type, LocalDateTime date) {
        return prizeRepository.findByTypeAndStartDateBefore(type, date);
    }

    @Override
    public String openPrizesPage(Model model, HttpSession session, PrizeType type) {
        setLoggedInAttribute(model, session, userService);

        List<Prize> prizes;
        if (type != null) {
            if (type.equals(PrizeType.INSTANT) || type.equals(PrizeType.RAFFLE)) {
                prizes = getPrizesByTypeAndRemainedTicketsGreaterThanAndStartDateBefore(type, 0, LocalDateTime.now());
            } else {
                prizes = getPrizesByTypeAndStartDateBefore(type, LocalDateTime.now());
            }
        } else {
            prizes = getAllPrizesWithRemainedTicketsGreaterThanAndStartDateBefore(0, LocalDateTime.now());
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

    @Override
    public String openAddPrizesPage(Model model, HttpSession session) {
        Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
        if (loggedIn == null) {
            loggedIn = false;
        }
        session.setAttribute("loggedIn", loggedIn);
        model.addAttribute("loggedIn", loggedIn);

        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            Optional<User> user = userService.getUserById(userId);
            if (user.isPresent()) {
                String role = String.valueOf(user.get().getRole());
                if (role != null && role.equals("ADMIN")) {
                    Integer totalPoints = user.get().getTotalPoints();
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
        List<Prize> prizes = getAllPrizes();
        model.addAttribute("prizes", prizes);
        return "redirect:/home";
    }

    @Override
    public String openRafflePage(Model model, HttpSession session, Long prizeId) {
        Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
        if (loggedIn == null) {
            loggedIn = false;
        }
        session.setAttribute("loggedIn", loggedIn);
        model.addAttribute("loggedIn", loggedIn);

        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            Optional<User> user = userService.getUserById(userId);
            if (user.isPresent()) {
                String role = String.valueOf(user.get().getRole());
                if (role != null && role.equals("ADMIN")) {
                    session.setAttribute("loggedUser", user);
                    model.addAttribute("loggedUser", user);

                    Integer totalPoints = user.get().getTotalPoints();
                    session.setAttribute("totalPoints", totalPoints);
                    model.addAttribute("totalPoints", totalPoints);

                    List<Prize> prizesWithoutWinners = getPrizesWithoutWinners();
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

                        int remainingTickets = getPrizeById(prizeId).get().getRemainedTickets();

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

    @Transactional
    @Override
    public ResponseEntity<String> chooseWinner(Long prizeId, int randomNumber) {
        List<User> participants = userService.getParticipantsByPrizeId(prizeId);
        if (randomNumber > participants.size() || randomNumber <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Невалидно случайно число!");
        }

        User winner = participants.get(randomNumber - 1);

        setWinnerForPrize(prizeId, winner.getId());

        return ResponseEntity.ok("Потребителят е свързан с наградата успешно!");
    }

    @Override
    public ResponseEntity<String> setPrizeForWinner(Long prizeId, Long purchaseId, Long userCode) {
        Prize prize = getPrizeById(prizeId).orElse(null);
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

    @Transactional
    @Override
    public ResponseEntity<String> takePrize(Long prizeId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Integer totalPoints = (Integer) session.getAttribute("totalPoints");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Влезте в своя профил, за да продължите напред.");
        }

        Optional<Prize> optionalPrize = getPrizeById(prizeId);
        if (optionalPrize.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Наградата не е намерена.");
        }

        Prize prize = optionalPrize.get();
        Integer neededPoints = prize.getNeededPointsToBuy();

        if (totalPoints < neededPoints) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Нямате достатъчно точки, за да закупите билет за тази награда.");
        }

        boolean purchaseSuccessful = purchasePrize(userId, prizeId);

        if (purchaseSuccessful) {
            session.setAttribute("totalPoints", totalPoints - neededPoints);
            return ResponseEntity.ok("Билетът за тази награда е закупен успешно!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Възникна грешка, моля опитайте отново!");
        }
    }

    @Transactional
    @Override
    public ResponseEntity<String> addPrizeToDB(String name, String description, Integer neededPointsToBuy, Integer totalTickets, String startDateTime, PrizeType type) {
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

        Long prizeCode = generateUniquePrizeCode();
        prize.setPrizeCode(prizeCode);

        savePrize(prize);

        return ResponseEntity.status(HttpStatus.CREATED).body("Вие добавихте наградата успешно!");
    }
}