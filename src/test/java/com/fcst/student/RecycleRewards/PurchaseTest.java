package com.fcst.student.RecycleRewards;

import com.fcst.student.RecycleRewards.model.Prize;
import com.fcst.student.RecycleRewards.model.Purchase;
import com.fcst.student.RecycleRewards.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PurchaseTest {

    @Test
    public void testNoArgsConstructor() {
        Purchase purchase = new Purchase();
        assertNotNull(purchase);
    }

    @Test
    public void testAllArgsConstructor() {
        Long id = 1L;
        User user = new User();
        Prize prize = new Prize();
        LocalDateTime purchaseDate = LocalDateTime.now();
        String winnerCode = "WIN123";
        LocalDateTime dateTakenPrize = LocalDateTime.now().plusDays(1);

        Purchase purchase = new Purchase(id, user, prize, purchaseDate, winnerCode, dateTakenPrize);

        assertEquals(id, purchase.getId());
        assertEquals(user, purchase.getUser());
        assertEquals(prize, purchase.getPrize());
        assertEquals(purchaseDate, purchase.getPurchaseDate());
        assertEquals(winnerCode, purchase.getWinnerCode());
        assertEquals(dateTakenPrize, purchase.getDateTakenPrize());
    }

    @Test
    public void testGettersAndSetters() {
        Long id = 1L;
        User user = new User();
        Prize prize = new Prize();
        LocalDateTime purchaseDate = LocalDateTime.now();
        String winnerCode = "WIN123";
        LocalDateTime dateTakenPrize = LocalDateTime.now().plusDays(1);

        Purchase purchase = new Purchase();

        purchase.setId(id);
        purchase.setUser(user);
        purchase.setPrize(prize);
        purchase.setPurchaseDate(purchaseDate);
        purchase.setWinnerCode(winnerCode);
        purchase.setDateTakenPrize(dateTakenPrize);

        assertEquals(id, purchase.getId());
        assertEquals(user, purchase.getUser());
        assertEquals(prize, purchase.getPrize());
        assertEquals(purchaseDate, purchase.getPurchaseDate());
        assertEquals(winnerCode, purchase.getWinnerCode());
        assertEquals(dateTakenPrize, purchase.getDateTakenPrize());
    }

    @Test
    public void testWinnerCodeGetterSetter() {
        Purchase purchase = new Purchase();
        String winnerCode = "WIN123";

        purchase.setWinnerCode(winnerCode);
        assertEquals(winnerCode, purchase.getWinnerCode());
    }

    @Test
    public void testDateTakenPrizeGetterSetter() {
        Purchase purchase = new Purchase();
        LocalDateTime dateTakenPrize = LocalDateTime.now().plusDays(1);

        purchase.setDateTakenPrize(dateTakenPrize);
        assertEquals(dateTakenPrize, purchase.getDateTakenPrize());
    }
}
