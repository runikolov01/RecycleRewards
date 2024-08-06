package com.fcst.student.RecycleRewards.UnitTests;

import com.fcst.student.RecycleRewards.model.Prize;
import com.fcst.student.RecycleRewards.model.Purchase;
import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.model.enums.PrizeType;
import com.fcst.student.RecycleRewards.repository.PrizeRepository;
import com.fcst.student.RecycleRewards.repository.PurchaseRepository;
import com.fcst.student.RecycleRewards.repository.UserRepository;
import com.fcst.student.RecycleRewards.service.PurchaseService;
import com.fcst.student.RecycleRewards.service.UserService;
import com.fcst.student.RecycleRewards.service.impl.PrizeServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PrizeServiceImplTest {

    @InjectMocks
    private PrizeServiceImpl prizeService;

    @Mock
    private UserService userService;

    @Mock
    private PurchaseService purchaseService;

    @Mock
    private PrizeRepository prizeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private HttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSavePrize() {
        Prize prize = new Prize();
        prizeService.savePrize(prize);
        verify(prizeRepository, times(1)).save(prize);
    }

    @Test
    void testGetPrizeById() {
        Long prizeId = 1L;
        Prize prize = new Prize();
        when(prizeRepository.findById(prizeId)).thenReturn(Optional.of(prize));
        Optional<Prize> result = prizeService.getPrizeById(prizeId);
        assertTrue(result.isPresent());
        assertEquals(prize, result.get());
    }

    @Test
    void testPurchasePrizeSuccess() {
        Long userId = 1L;
        Long prizeId = 1L;
        User user = new User();
        user.setTotalPoints(100);
        Prize prize = new Prize();
        prize.setNeededPointsToBuy(50);
        prize.setRemainedTickets(10);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(prizeRepository.findById(prizeId)).thenReturn(Optional.of(prize));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(new Purchase());

        boolean result = prizeService.purchasePrize(userId, prizeId);
        assertTrue(result);
        assertEquals(50, user.getTotalPoints());
        assertEquals(9, prize.getRemainedTickets());
    }

    @Test
    void testPurchasePrizeFailureDueToInsufficientPoints() {
        Long userId = 1L;
        Long prizeId = 1L;
        User user = new User();
        user.setTotalPoints(10);
        Prize prize = new Prize();
        prize.setNeededPointsToBuy(50);
        prize.setRemainedTickets(10);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(prizeRepository.findById(prizeId)).thenReturn(Optional.of(prize));

        boolean result = prizeService.purchasePrize(userId, prizeId);
        assertFalse(result);
    }

//    @Test
//    void testSetWinnerForPrize() {
//        Long prizeId = 1L;
//        Long userId = 2L;
//        Prize prize = new Prize();
//        User user = new User();
//        when(prizeRepository.findById(prizeId)).thenReturn(Optional.of(prize));
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//
//        prizeService.setWinnerForPrize(prizeId, userId);
//        verify(prizeRepository, times(1)).save(prize);
//        assertTrue(prize.getParticipants().contains(user));
//        assertEquals(0, prize.getRemainedTickets());
//        assertNotNull(prize.getEndDate());
//    }

    @Test
    void testChooseWinnerSuccess() {
        Long prizeId = 1L;
        int randomNumber = 1;
        User user = new User();
        List<User> participants = Collections.singletonList(user);
        when(userService.getParticipantsByPrizeId(prizeId)).thenReturn(participants);

        ResponseEntity<String> response = prizeService.chooseWinner(prizeId, randomNumber);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Потребителят е свързан с наградата успешно!", response.getBody());
    }

    @Test
    void testChooseWinnerFailureInvalidNumber() {
        Long prizeId = 1L;
        int randomNumber = 2; // Invalid as there is only one participant
        User user = new User();
        List<User> participants = Collections.singletonList(user);
        when(userService.getParticipantsByPrizeId(prizeId)).thenReturn(participants);

        ResponseEntity<String> response = prizeService.chooseWinner(prizeId, randomNumber);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Невалидно случайно число!", response.getBody());
    }

    @Test
    void testAddPrizeToDB() {
        String name = "Test Prize";
        String description = "Test Description";
        Integer neededPointsToBuy = 10;
        Integer totalTickets = 100;
        String startDateTime = "2024-08-03T10:00";
        PrizeType type = PrizeType.INSTANT;

        when(prizeRepository.save(any(Prize.class))).thenReturn(new Prize());

        ResponseEntity<String> response = prizeService.addPrizeToDB(name, description, neededPointsToBuy, totalTickets, startDateTime, type);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Вие добавихте наградата успешно!", response.getBody());
    }
}
