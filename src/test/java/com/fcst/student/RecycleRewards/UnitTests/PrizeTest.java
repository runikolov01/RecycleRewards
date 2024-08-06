package com.fcst.student.RecycleRewards.UnitTests;

import com.fcst.student.RecycleRewards.model.Prize;
import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.model.enums.PrizeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrizeTest {
    private Prize prize;

    @BeforeEach
    public void setUp() {
        LocalDateTime now = LocalDateTime.now();
        prize = new Prize(
                1L,
                12345L,
                "PrizeName",
                "PrizeDescription",
                100,
                200,
                150,
                now,
                new ArrayList<>(),
                PrizeType.RAFFLE
        );
    }

    @Test
    public void testDefaultConstructor() {
        Prize defaultPrize = new Prize();
        assertNull(defaultPrize.getId());
        assertNull(defaultPrize.getPrizeCode());
        assertNull(defaultPrize.getName());
        assertNull(defaultPrize.getDescription());
        assertNull(defaultPrize.getNeededPointsToBuy());
        assertNull(defaultPrize.getTotalTickets());
        assertEquals(0, defaultPrize.getRemainedTickets());
        assertNull(defaultPrize.getStartDate());
        assertNull(defaultPrize.getEndDate());
        assertNull(defaultPrize.getParticipants());
        assertNull(defaultPrize.getType());
    }

    @Test
    public void testConstructorWithAllFields() {
        LocalDateTime now = LocalDateTime.now();
        List<User> participants = new ArrayList<>();
        Prize testPrize = new Prize(
                1L,
                12345L,
                "PrizeName",
                "PrizeDescription",
                100,
                200,
                150,
                now,
                participants,
                PrizeType.RAFFLE
        );
        assertEquals(1L, testPrize.getId());
        assertEquals(12345L, testPrize.getPrizeCode());
        assertEquals("PrizeName", testPrize.getName());
        assertEquals("PrizeDescription", testPrize.getDescription());
        assertEquals(100, testPrize.getNeededPointsToBuy());
        assertEquals(200, testPrize.getTotalTickets());
        assertEquals(150, testPrize.getRemainedTickets());
        assertEquals(now, testPrize.getStartDate());
        assertEquals(participants, testPrize.getParticipants());
        assertEquals(PrizeType.RAFFLE, testPrize.getType());
    }

    @Test
    public void testGettersAndSetters() {
        LocalDateTime now = LocalDateTime.now();
        List<User> participants = new ArrayList<>();

        prize.setId(2L);
        assertEquals(2L, prize.getId());

        prize.setPrizeCode(54321L);
        assertEquals(54321L, prize.getPrizeCode());

        prize.setName("NewPrizeName");
        assertEquals("NewPrizeName", prize.getName());

        prize.setDescription("NewPrizeDescription");
        assertEquals("NewPrizeDescription", prize.getDescription());

        prize.setNeededPointsToBuy(150);
        assertEquals(150, prize.getNeededPointsToBuy());

        prize.setTotalTickets(300);
        assertEquals(300, prize.getTotalTickets());

        prize.setRemainedTickets(200);
        assertEquals(200, prize.getRemainedTickets());

        prize.setStartDate(now);
        assertEquals(now, prize.getStartDate());

        prize.setEndDate(now.plusDays(10));
        assertEquals(now.plusDays(10), prize.getEndDate());

        prize.setParticipants(participants);
        assertEquals(participants, prize.getParticipants());

        prize.setType(PrizeType.RAFFLE);
        assertEquals(PrizeType.RAFFLE, prize.getType());
    }

    @Test
    public void testAddAndRemoveParticipant() {
        User user = new User();
        List<User> participants = new ArrayList<>();
        prize.setParticipants(participants);

        prize.addParticipant(user);
        assertTrue(prize.getParticipants().contains(user));

        prize.removeParticipant(user);
        assertFalse(prize.getParticipants().contains(user));
    }
}