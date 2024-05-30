package com.fcst.student.RecycleRewards.service;

import com.fcst.student.RecycleRewards.model.Prize;
import com.fcst.student.RecycleRewards.model.enums.PrizeType;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public interface PrizeService {
    void savePrize(Prize prize);

    Optional<Prize> getPrizeById(Long id);

    List<Prize> getAllPrizes();

    boolean purchasePrize(Long userId, Long prizeId);

    void setWinnerForPrize(Long prizeId, Long userId);

    List<Prize> getPrizesWithoutWinners();

    List<Prize> getPrizesByTypeAndStartDateBefore(PrizeType type, LocalDateTime date);

    List<Prize> getAllPrizesWithRemainedTicketsGreaterThanAndStartDateBefore(Integer tickets, LocalDateTime date);

    List<Prize> getPrizesByTypeAndRemainedTicketsGreaterThanAndStartDateBefore(PrizeType type, Integer tickets, LocalDateTime date);

    Long generateUniquePrizeCode();

    Long generateRandomCode();

    String openPrizesPage(Model model, HttpSession session, PrizeType type);

    String openAddPrizesPage(Model model, HttpSession session);

    String openRafflePage(Model model, HttpSession session, Long prizeId);

    ResponseEntity<String> chooseWinner(Long prizeId, int randomNumber);

    ResponseEntity<String> setPrizeForWinner(Long prizeId, Long purchaseId, Long userCode);

    ResponseEntity<String> takePrize(Long prizeId, HttpSession session);

    ResponseEntity<String> addPrizeToDB(String name, String description, Integer neededPointsToBuy, Integer totalTickets, String startDateTime, PrizeType type);
}