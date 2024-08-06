package com.fcst.student.RecycleRewards.UnitTests;

import com.fcst.student.RecycleRewards.model.Ticket;
import com.fcst.student.RecycleRewards.model.User;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TicketTest {

    @Test
    public void testTicketGettersAndSetters() {
        LocalDateTime now = LocalDateTime.now();
        Ticket ticket = new Ticket();

        ticket.setId(1L);
        ticket.setNumber("12345");
        ticket.setIssued(now);
        ticket.setActiveUntil(now.plusHours(72));
        ticket.setBottles(10);
        ticket.setPoints(50);
        ticket.setRegisteredOn(now.plusHours(1));
        ticket.setRegisteredBy(new User());
        ticket.setVoucher(true);

        // Assert
        assertEquals(1L, ticket.getId());
        assertEquals("12345", ticket.getNumber());
        assertEquals(now, ticket.getIssued());
        assertEquals(now.plusHours(72), ticket.getActiveUntil());
        assertEquals(10, ticket.getBottles());
        assertEquals(50, ticket.getPoints());
        assertEquals(now.plusHours(1), ticket.getRegisteredOn());
        assertNotNull(ticket.getRegisteredBy());
        assertTrue(ticket.getVoucher());
    }

    @Test
    public void testTicketConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Ticket ticket = new Ticket("12345", now, now.plusHours(72), 10, 50, true);

        assertEquals("12345", ticket.getNumber());
        assertEquals(now, ticket.getIssued());
        assertEquals(now.plusHours(72), ticket.getActiveUntil());
        assertEquals(10, ticket.getBottles());
        assertEquals(50, ticket.getPoints());
        assertTrue(ticket.getVoucher());
    }

    @Test
    public void testNoArgsConstructor() {
        Ticket ticket = new Ticket();
        assertNotNull(ticket);
    }

    @Test
    public void testConstructorWithNumber() {
        Ticket ticket = new Ticket("ABC123");
        assertEquals("ABC123", ticket.getNumber());
    }

    @Test
    public void testConstructorWithAllArgs() {
        LocalDateTime issued = LocalDateTime.now();
        LocalDateTime activeUntil = issued.plusDays(3);
        LocalDateTime registeredOn = issued.plusDays(1);
        User user = new User();

        Ticket ticket = new Ticket("ABC123", issued, activeUntil, 10, 50, false);

        assertEquals("ABC123", ticket.getNumber());
        assertEquals(issued, ticket.getIssued());
        assertEquals(activeUntil, ticket.getActiveUntil());
        assertEquals(10, ticket.getBottles());
        assertEquals(50, ticket.getPoints());
        assertFalse(ticket.getVoucher());
    }

    @Test
    public void testFullConstructor() {
        LocalDateTime issued = LocalDateTime.now();
        LocalDateTime activeUntil = issued.plusDays(3);
        LocalDateTime registeredOn = issued.plusDays(1);
        User user = new User();

        Ticket ticket = new Ticket(1L, "ABC123", issued, activeUntil, 10, 50, registeredOn, user);

        assertEquals(1L, ticket.getId());
        assertEquals("ABC123", ticket.getNumber());
        assertEquals(issued, ticket.getIssued());
        assertEquals(activeUntil, ticket.getActiveUntil());
        assertEquals(10, ticket.getBottles());
        assertEquals(50, ticket.getPoints());
        assertEquals(registeredOn, ticket.getRegisteredOn());
        assertEquals(user, ticket.getRegisteredBy());
    }

    @Test
    public void testGettersAndSetters() {
        LocalDateTime issued = LocalDateTime.now();
        LocalDateTime activeUntil = issued.plusDays(3);
        LocalDateTime registeredOn = issued.plusDays(1);
        User user = new User();

        Ticket ticket = new Ticket();

        ticket.setId(1L);
        ticket.setNumber("ABC123");
        ticket.setIssued(issued);
        ticket.setActiveUntil(activeUntil);
        ticket.setBottles(10);
        ticket.setPoints(50);
        ticket.setRegisteredOn(registeredOn);
        ticket.setRegisteredBy(user);
        ticket.setVoucher(true);

        assertEquals(1L, ticket.getId());
        assertEquals("ABC123", ticket.getNumber());
        assertEquals(issued, ticket.getIssued());
        assertEquals(activeUntil, ticket.getActiveUntil());
        assertEquals(10, ticket.getBottles());
        assertEquals(50, ticket.getPoints());
        assertEquals(registeredOn, ticket.getRegisteredOn());
        assertEquals(user, ticket.getRegisteredBy());
        assertTrue(ticket.getVoucher());
    }

    @Test
    public void testVoucherGetterSetter() {
        Ticket ticket = new Ticket();
        ticket.setVoucher(true);
        assertTrue(ticket.getVoucher());
        ticket.setVoucher(false);
        assertFalse(ticket.getVoucher());
    }
}