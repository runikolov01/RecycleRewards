package com.fcst.student.RecycleRewards;

import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.repository.PrizeRepository;
import com.fcst.student.RecycleRewards.repository.TicketRepository;
import com.fcst.student.RecycleRewards.repository.UserRepository;
import com.fcst.student.RecycleRewards.service.AddressService;
import com.fcst.student.RecycleRewards.service.EmailService;
import com.fcst.student.RecycleRewards.service.PurchaseService;
import com.fcst.student.RecycleRewards.service.impl.UserServiceImpl;
import com.fcst.student.RecycleRewards.service.session.LoggedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private Environment environment;

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AddressService addressService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PurchaseService purchaseService;

    @Mock
    private EmailConfiguration emailConfiguration;

    @Mock
    private EmailService emailService;

    @Mock
    private PrizeRepository prizeRepository;

    @Mock
    private LoggedUser loggedUser;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        userService.saveUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        userService.updateUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testDeleteUser() {
        Long id = 1L;
        userService.deleteUser(id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    public void testGetUserById() {
        Long userId = 1L;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(userId);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testGetUserByEmail() {
        String email = "test@example.com";
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(user);

        User result = userService.getUserByEmail(email);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testFindByResetToken() {
        String resetToken = "resetToken";
        User user = new User();
        when(userRepository.findByResetToken(resetToken)).thenReturn(user);

        User result = userService.findByResetToken(resetToken);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).findByResetToken(resetToken);
    }

    @Test
    public void testVerifyPassword() {
        User user = new User();
        user.setPassword("encodedPassword");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        boolean result = userService.verifyPassword(user, "password");

        assertTrue(result);
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
    }

    @Test
    public void testLogout() {
        userService.logout();
        verify(loggedUser, times(1)).reset();
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = List.of(new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(users, result);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testGetParticipantsByPrizeId() {
        Long prizeId = 1L;
        List<User> users = List.of(new User());
        when(userRepository.findAllByPrizeId(prizeId)).thenReturn(users);

        List<User> result = userService.getParticipantsByPrizeId(prizeId);

        assertNotNull(result);
        assertEquals(users, result);
        verify(userRepository, times(1)).findAllByPrizeId(prizeId);
    }

    @Test
    public void testGetTotalBottlesForAllUsers() {
        int totalBottles = 100;
        when(ticketRepository.getTotalBottlesInSystem()).thenReturn(totalBottles);

        int result = userService.getTotalBottlesForAllUsers();

        assertEquals(totalBottles, result);
        verify(ticketRepository, times(1)).getTotalBottlesInSystem();
    }

    @Test
    public void testFindByActivationToken() {
        String token = "activationToken";
        User user = new User();
        when(userRepository.findByActivationToken(token)).thenReturn(user);

        User result = userService.findByActivationToken(token);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).findByActivationToken(token);
    }

//    @Test
//    public void testGenerateUniqueUserCode() {
//        Long uniqueCode = 12345678L;
//        when(userRepository.existsByUserCode(uniqueCode)).thenReturn(false);
//        // Ensure generateRandomCode() returns uniqueCode
//        doReturn(uniqueCode).when(userService).generateRandomCode();
//
//        Long result = userService.generateUniqueUserCode();
//
//        assertEquals(uniqueCode, result);
//        verify(userRepository, times(1)).existsByUserCode(uniqueCode);
//    }

    @Test
    public void testGenerateRandomCode() {
        // Call the method directly and check if the generated code is in the expected range
        Long result = userService.generateRandomCode();

        assertTrue(result >= 10000000L && result < 100000000L, "Generated code is not in the expected range");
    }

    @Test
    public void testGetUserByUserCode() {
        Long userCode = 12345678L;
        User user = new User();
        when(userRepository.findByUserCode(userCode)).thenReturn(user);

        User result = userService.getUserByUserCode(userCode);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).findByUserCode(userCode);
    }
}