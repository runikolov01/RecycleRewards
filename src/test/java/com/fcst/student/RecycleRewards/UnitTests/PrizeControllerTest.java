package com.fcst.student.RecycleRewards.UnitTests;

import com.fcst.student.RecycleRewards.model.enums.PrizeType;
import com.fcst.student.RecycleRewards.service.PrizeService;
import com.fcst.student.RecycleRewards.web.PrizeController;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PrizeControllerTest {

    @Mock
    private PrizeService prizeService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private PrizeController prizeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowPrizes() {
        PrizeType type = PrizeType.RAFFLE;
        when(prizeService.openPrizesPage(model, session, type)).thenReturn("prizesPage");

        String viewName = prizeController.showPrizes(model, session, type);

        assertEquals("prizesPage", viewName);
        verify(prizeService, times(1)).openPrizesPage(model, session, type);
    }

    @Test
    void testShowAddPrizesPage() {
        when(prizeService.openAddPrizesPage(model, session)).thenReturn("addPrizesPage");

        String viewName = prizeController.showAddPrizesPage(model, session);

        assertEquals("addPrizesPage", viewName);
        verify(prizeService, times(1)).openAddPrizesPage(model, session);
    }

    @Test
    void testShowRafflePage() {
        Long prizeId = 1L;
        when(prizeService.openRafflePage(model, session, prizeId)).thenReturn("rafflePage");

        String viewName = prizeController.showRafflePage(model, session, prizeId);

        assertEquals("rafflePage", viewName);
        verify(prizeService, times(1)).openRafflePage(model, session, prizeId);
    }

    @Test
    void testDrawWinner() {
        Long prizeId = 1L;
        int randomNumber = 42;
        ResponseEntity<String> response = ResponseEntity.ok("winner");
        when(prizeService.chooseWinner(prizeId, randomNumber)).thenReturn(response);

        ResponseEntity<String> result = prizeController.drawWinner(prizeId, randomNumber);

        assertEquals(response, result);
        verify(prizeService, times(1)).chooseWinner(prizeId, randomNumber);
    }

    @Test
    void testConnectPrizeWithWinner() {
        Long prizeId = 1L;
        Long purchaseId = 2L;
        Long userCode = 3L;
        ResponseEntity<String> response = ResponseEntity.ok("connected");
        when(prizeService.setPrizeForWinner(prizeId, purchaseId, userCode)).thenReturn(response);

        ResponseEntity<String> result = prizeController.connectPrizeWithWinner(prizeId, purchaseId, userCode);

        assertEquals(response, result);
        verify(prizeService, times(1)).setPrizeForWinner(prizeId, purchaseId, userCode);
    }

    @Test
    void testBuyPrize() {
        Long prizeId = 1L;
        ResponseEntity<String> response = ResponseEntity.ok("bought");
        when(prizeService.takePrize(prizeId, session)).thenReturn(response);

        ResponseEntity<String> result = prizeController.buyPrize(prizeId, session);

        assertEquals(response, result);
        verify(prizeService, times(1)).takePrize(prizeId, session);
    }

    @Test
    void testSavePrize() {
        String name = "Prize Name";
        String description = "Description";
        Integer neededPoints = 100;
        Integer totalTickets = 10;
        String startDateTime = "2024-08-04T10:00:00";
        PrizeType type = PrizeType.RAFFLE;
        ResponseEntity<String> response = ResponseEntity.ok("saved");
        when(prizeService.addPrizeToDB(name, description, neededPoints, totalTickets, startDateTime, type)).thenReturn(response);

        ResponseEntity<String> result = prizeController.savePrize(name, description, neededPoints, totalTickets, startDateTime, type);

        assertEquals(response, result);
        verify(prizeService, times(1)).addPrizeToDB(name, description, neededPoints, totalTickets, startDateTime, type);
    }
}