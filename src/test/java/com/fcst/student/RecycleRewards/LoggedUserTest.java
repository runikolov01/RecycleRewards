package com.fcst.student.RecycleRewards;

import com.fcst.student.RecycleRewards.service.session.LoggedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LoggedUserTest {
    private LoggedUser loggedUser;

    @BeforeEach
    public void setUp() {
        loggedUser = new LoggedUser();
    }

    @Test
    public void testInitialState() {
        assertNull(loggedUser.getLoggedUserId(), "Initial loggedUserId should be null");
    }

    @Test
    public void testSetLoggedUserId() {
        loggedUser.setLoggedUserId(123L);
        assertEquals(123L, loggedUser.getLoggedUserId(), "LoggedUserId should be 123L");
    }

    @Test
    public void testGetLoggedUserId() {
        loggedUser.setLoggedUserId(456L);
        assertEquals(456L, loggedUser.getLoggedUserId(), "LoggedUserId should be 456L");
    }

    @Test
    public void testReset() {
        loggedUser.setLoggedUserId(789L);
        loggedUser.reset();
        assertNull(loggedUser.getLoggedUserId(), "LoggedUserId should be null after reset");
    }
}