package com.fcst.student.RecycleRewards;

import com.fcst.student.RecycleRewards.model.PrizeDetailsDto;
import com.fcst.student.RecycleRewards.model.Purchase;
import com.fcst.student.RecycleRewards.repository.PurchaseRepository;
import com.fcst.student.RecycleRewards.service.impl.PurchaseServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PurchaseServiceImplTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testGetPurchasesByUserId() {
        Long userId = 1L;
        List<Purchase> purchases = new ArrayList<>();
        when(purchaseRepository.findByUserId(userId)).thenReturn(purchases);

        List<Purchase> result = purchaseService.getPurchasesByUserId(userId);

        assertNotNull(result);
        assertEquals(purchases, result);
        verify(purchaseRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void testGetAllPurchasesByPrizeId() {
        Long prizeId = 1L;
        List<Purchase> purchases = new ArrayList<>();
        when(purchaseRepository.findAllByPrizeId(prizeId)).thenReturn(purchases);

        List<Purchase> result = purchaseService.getAllPurchasesByPrizeId(prizeId);

        assertNotNull(result);
        assertEquals(purchases, result);
        verify(purchaseRepository, times(1)).findAllByPrizeId(prizeId);
    }

    @Test
    public void testGetPurchaseById() {
        Long id = 1L;
        Purchase purchase = new Purchase();
        when(purchaseRepository.findById(id)).thenReturn(Optional.of(purchase));

        Purchase result = purchaseService.getPurchaseById(id);

        assertNotNull(result);
        assertEquals(purchase, result);
        verify(purchaseRepository, times(1)).findById(id);
    }

//    @Test
//    public void testGenerateUniquePurchaseWinnerCode() {
//        String uniqueCode = "A1B2C3D4";
//        when(purchaseRepository.existsByWinnerCode(uniqueCode)).thenReturn(false);
//        RandomGenerator randomGenerator = mock(RandomGenerator.class);
//        when(randomGenerator.nextInt(anyInt())).thenReturn(0); // Mock predictable index
//        PurchaseServiceImpl purchaseServiceWithMockRandom = new PurchaseServiceImpl(purchaseRepository, randomGenerator);
//
//        String result = purchaseServiceWithMockRandom.generateUniquePurchaseWinnerCode();
//
//        assertEquals(uniqueCode, result);
//        verify(purchaseRepository, times(1)).existsByWinnerCode(uniqueCode);
//    }

    @Test
    public void testGetPrizeDetailsForUser() {
        Long userId = 1L;
        List<PrizeDetailsDto> prizeDetails = new ArrayList<>();
        when(purchaseRepository.findPrizeDetailsByUserId(userId)).thenReturn(prizeDetails);

        List<PrizeDetailsDto> result = purchaseService.getPrizeDetailsForUser(userId);

        assertNotNull(result);
        assertEquals(prizeDetails, result);
        verify(purchaseRepository, times(1)).findPrizeDetailsByUserId(userId);
    }

    @Test
    public void testShowUserPurchasedTicketsWithUserId() {
        Long userId = 1L;
        List<Purchase> purchases = new ArrayList<>();
        when(session.getAttribute("userId")).thenReturn(userId);
        when(purchaseRepository.findByUserId(userId)).thenReturn(purchases);

        ResponseEntity<List<Purchase>> response = purchaseService.showUserPurchasedTickets(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(purchases, response.getBody());
        verify(purchaseRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void testShowUserPurchasedTicketsWithoutUserId() {
        when(session.getAttribute("userId")).thenReturn(null);

        ResponseEntity<List<Purchase>> response = purchaseService.showUserPurchasedTickets(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }
}