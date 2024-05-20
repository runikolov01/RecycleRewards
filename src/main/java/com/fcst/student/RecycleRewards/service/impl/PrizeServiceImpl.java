package com.fcst.student.RecycleRewards.service.impl;

import com.fcst.student.RecycleRewards.model.Prize;
import com.fcst.student.RecycleRewards.model.Purchase;
import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.model.enums.PrizeType;
import com.fcst.student.RecycleRewards.repository.PrizeRepository;
import com.fcst.student.RecycleRewards.repository.PurchaseRepository;
import com.fcst.student.RecycleRewards.repository.UserRepository;
import com.fcst.student.RecycleRewards.service.PrizeService;
import com.fcst.student.RecycleRewards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PrizeServiceImpl implements PrizeService {
    @Autowired
    private final PrizeRepository prizeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private UserService userService;


    public PrizeServiceImpl(PrizeRepository prizeRepository) {
        this.prizeRepository = prizeRepository;
    }

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

    @Override
    public List<Prize> getPrizesByType(PrizeType type) {
        return prizeRepository.findByType(type);
    }

    @Override
    public void deletePrizeById(int id) {
    }

    @Override
    public boolean purchasePrize(Long userId, Long prizeId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Prize> prizeOpt = prizeRepository.findById(prizeId);

        if (!userOpt.isPresent() || !prizeOpt.isPresent()) {
            return false;
        }

        User user = userOpt.get();
        Prize prize = prizeOpt.get();
        int remainedTicketsForThisPrize = prize.getRemainedTickets();

        if (user.getTotalPoints() < prize.getNeededPointsToBuy() || remainedTicketsForThisPrize <= 0) {
            return false;
        }

        // Deduct points
        user.setTotalPoints(user.getTotalPoints() - prize.getNeededPointsToBuy());
        prize.setRemainedTickets(remainedTicketsForThisPrize - 1);
        userRepository.save(user);

        // Record purchase
        Purchase purchase = new Purchase();
        if (prize.getType() == PrizeType.INSTANT) {
            user.getPrizes().add(prize);

        }
        purchase.setUser(user);
        purchase.setPrize(prize);
        purchase.setPurchaseDate(LocalDateTime.now());
        purchaseRepository.save(purchase);

        return true;
    }

    @Override
    public void setWinnerForPrize(Long prizeId, Long userId) {
        Optional<Prize> optionalPrize = prizeRepository.findById(prizeId);
        Optional<User> optionalWinner = userRepository.findById(userId);
        if (optionalPrize.isPresent() && optionalWinner.isPresent()) {
            Prize prize = optionalPrize.get();
            User winner = optionalWinner.get();

            // Add the winner to the prize's list of winners
//            prize.addWinner(winner);

            // Save the updated prize
            prizeRepository.save(prize);
        }
    }


    @Override
    public List<Prize> getPrizesWithoutWinners() {
        return prizeRepository.findPrizesWithoutWinners();
    }
}