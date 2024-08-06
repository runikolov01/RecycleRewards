package com.fcst.student.RecycleRewards.UnitTests;

import com.fcst.student.RecycleRewards.model.Address;
import com.fcst.student.RecycleRewards.model.Prize;
import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.model.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;
    private Address address;
    private List<Prize> prizes;

    @BeforeEach
    public void setUp() {
        address = new Address(1L, "City", 12345, "Street", 1, 2, 3);
        prizes = new ArrayList<>();
        user = new User(1L, 1000L, "John", "Doe", 10, address, "john.doe@example.com", "password", 123456789, Role.CLIENT, prizes);
    }

    @Test
    public void testDefaultConstructor() {
        User user = new User();
        assertNull(user.getId());
        assertNull(user.getUserCode());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertEquals(0, user.getTotalPoints());
        assertNull(user.getAddress());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getPhone());
        assertNull(user.getRole());
        assertNull(user.getPrizes());
    }

    @Test
    public void testConstructorWithAllFields() {
        LocalDateTime now = LocalDateTime.now();
        user = new User(1L, 1000L, "token", now, true, "John", "Doe", 10, address, "john.doe@example.com", "password", now, 123456789, Role.CLIENT, prizes, 20);
        assertEquals(1L, user.getId());
        assertEquals(1000L, user.getUserCode());
        assertEquals("token", user.getActivationToken());
        assertEquals(now, user.getTokenExpiry());
        assertTrue(user.isActivated());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals(10, user.getTotalPoints());
        assertEquals(address, user.getAddress());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals(now, user.getRegistrationDate());
        assertEquals(123456789, user.getPhone());
        assertEquals(Role.CLIENT, user.getRole());
        assertEquals(prizes, user.getPrizes());
        assertEquals(20, user.getTotalBottles());
    }

    @Test
    public void testGettersAndSetters() {
        LocalDateTime now = LocalDateTime.now();
        user.setId(2L);
        assertEquals(2L, user.getId());

        user.setUserCode(2000L);
        assertEquals(2000L, user.getUserCode());

        user.setActivationToken("newToken");
        assertEquals("newToken", user.getActivationToken());

        user.setTokenExpiry(now);
        assertEquals(now, user.getTokenExpiry());

        user.setActivated(true);
        assertTrue(user.isActivated());

        user.setFirstName("Jane");
        assertEquals("Jane", user.getFirstName());

        user.setLastName("Smith");
        assertEquals("Smith", user.getLastName());

        user.setTotalPoints(20);
        assertEquals(20, user.getTotalPoints());

        Address newAddress = new Address(2L, "NewCity", 54321, "NewStreet", 10, 5, 6);
        user.setAddress(newAddress);
        assertEquals(newAddress, user.getAddress());

        user.setEmail("jane.smith@example.com");
        assertEquals("jane.smith@example.com", user.getEmail());

        user.setPassword("newPassword");
        assertEquals("newPassword", user.getPassword());

        user.setRegistrationDate(now);
        assertEquals(now, user.getRegistrationDate());

        user.setPhone(987654321);
        assertEquals(987654321, user.getPhone());

        user.setRole(Role.ADMIN);
        assertEquals(Role.ADMIN, user.getRole());

        List<Prize> newPrizes = new ArrayList<>();
        user.setPrizes(newPrizes);
        assertEquals(newPrizes, user.getPrizes());

        user.setTotalBottles(30);
        assertEquals(30, user.getTotalBottles());

        user.setResetToken("resetToken");
        assertEquals("resetToken", user.getResetToken());

        user.setDeletedDate(now);
        assertEquals(now, user.getDeletedDate());
    }

    @Test
    public void testAddPrize() {
        Prize prize = new Prize();
        user.addPrize(prize);
        assertEquals(1, user.getPrizes().size());
        assertTrue(user.getPrizes().contains(prize));
    }
}