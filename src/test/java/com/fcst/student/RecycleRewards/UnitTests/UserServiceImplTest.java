package com.fcst.student.RecycleRewards.UnitTests;

import com.fcst.student.RecycleRewards.EmailConfiguration;
import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.model.enums.Role;
import com.fcst.student.RecycleRewards.repository.PrizeRepository;
import com.fcst.student.RecycleRewards.repository.TicketRepository;
import com.fcst.student.RecycleRewards.repository.UserRepository;
import com.fcst.student.RecycleRewards.service.AddressService;
import com.fcst.student.RecycleRewards.service.EmailService;
import com.fcst.student.RecycleRewards.service.PurchaseService;
import com.fcst.student.RecycleRewards.service.impl.UserServiceImpl;
import com.fcst.student.RecycleRewards.service.session.LoggedUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.time.LocalDateTime;
import java.util.*;

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

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private UserServiceImpl userService;


    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RedirectAttributes redirectAttributes;

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

    @Test
    public void testOpenHomePageWhenLoggedInIsNull() {
        when(session.getAttribute("loggedIn")).thenReturn(null);
        when(session.getAttribute("userId")).thenReturn(null);
        when(userService.getTotalBottlesForAllUsers()).thenReturn(100);

        String viewName = userService.openHomePage(model, session);

        verify(session).setAttribute("loggedIn", false);
        verify(model).addAttribute("loggedIn", false);
        verify(model).addAttribute("totalBottles", 100);
        verify(model).addAttribute("totalKg", 100 * 0.015);
        assertEquals("home", viewName);
    }

    @Test
    public void testOpenHomePageWhenLoggedInIsTrueAndUserIdIsNotNull() {
        Long userId = 1L;
        User user = new User();
        user.setTotalBottles(200);
        user.setTotalPoints(50);

        when(session.getAttribute("loggedIn")).thenReturn(true);
        when(session.getAttribute("userId")).thenReturn(userId);
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));
        when(session.getAttribute("bottlesCount")).thenReturn(5);
        when(userService.getTotalBottlesForAllUsers()).thenReturn(100);

        String viewName = userService.openHomePage(model, session);

        verify(session).setAttribute("loggedIn", true);
        verify(model).addAttribute("loggedIn", true);
        verify(model).addAttribute("userTotalBottles", 200);
        verify(model).addAttribute("userKgBottles", 200 * 0.015);
        verify(model).addAttribute("bottlesCount", 5);
        verify(model).addAttribute("loggedUser", user);
        verify(model).addAttribute("totalPoints", 50);
        verify(model).addAttribute("totalBottles", 100);
        verify(model).addAttribute("totalKg", 100 * 0.015);
        assertEquals("home", viewName);
    }


    @Test
    public void testOpenRegisterFormWithoutInputFlashMap() {
        when(RequestContextUtils.getInputFlashMap(request)).thenReturn(null);
        when(session.getAttribute("loggedIn")).thenReturn(null);
        when(session.getAttribute("userId")).thenReturn(null);

        String viewName = userService.openRegisterForm(model, session, request);

        verify(model).addAttribute("loggedIn", false);
        verify(session).setAttribute("loggedIn", false);
        assertEquals("register", viewName);
    }

    @Test
    public void testOpenRegisterFormWhenUserIdNotNull() {
        when(session.getAttribute("loggedIn")).thenReturn(false);
        when(session.getAttribute("userId")).thenReturn(1L);

        String viewName = userService.openRegisterForm(model, session, request);

        assertEquals("redirect:/myProfile", viewName);
    }

    @Test
    public void testOpenLoginFormWithError() {
        when(session.getAttribute("loggedIn")).thenReturn(null);
        when(session.getAttribute("userId")).thenReturn(null);

        String viewName = userService.openLoginForm("error", model, session);

        verify(model).addAttribute("loggedIn", false);
        verify(model).addAttribute("error", true);
        assertEquals("login", viewName);
    }

    @Test
    public void testOpenLoginFormWithoutError() {
        when(session.getAttribute("loggedIn")).thenReturn(null);
        when(session.getAttribute("userId")).thenReturn(null);

        String viewName = userService.openLoginForm(null, model, session);

        verify(model).addAttribute("loggedIn", false);
        assertEquals("login", viewName);
    }

    @Test
    public void testOpenLoginFormWhenUserIdNotNull() {
        when(session.getAttribute("loggedIn")).thenReturn(true);
        when(session.getAttribute("userId")).thenReturn(1L);

        String viewName = userService.openLoginForm(null, model, session);

        assertEquals("redirect:/myProfile", viewName);
    }

    @Test
    public void testOpenMyProfileWhenUserIdIsNull() {
        when(session.getAttribute("userId")).thenReturn(null);

        String viewName = userService.openMyProfile(model, session);

        assertEquals("redirect:/login", viewName);
    }

    @Test
    public void testOpenMyProfileWhenUserNotFound() {
        when(session.getAttribute("userId")).thenReturn(1L);
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        String viewName = userService.openMyProfile(model, session);

        assertEquals("redirect:/login", viewName);
    }

    @Test
    public void testOpenMyProfileWhenUserFound() {
        Long userId = 1L;
        User user = new User();
        user.setTotalPoints(50);
        when(session.getAttribute("userId")).thenReturn(userId);
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));
        when(purchaseService.getPrizeDetailsForUser(userId)).thenReturn(Collections.emptyList());
        when(purchaseService.getPurchasesByUserId(userId)).thenReturn(Collections.emptyList());

        String viewName = userService.openMyProfile(model, session);

        verify(model).addAttribute("prizeDetails", Collections.emptyList());
        verify(model).addAttribute("totalPoints", 50);
        verify(model).addAttribute("loggedUser", user);
        verify(session).setAttribute("loggedUser", user);
        verify(model).addAttribute("loggedIn", true);
        verify(model).addAttribute("purchases", Collections.emptyList());
        assertEquals("myprofile", viewName);
    }

    @Test
    public void testOpenWinnersPageWhenLoggedInIsNull() {
        when(session.getAttribute("loggedIn")).thenReturn(null);
        when(session.getAttribute("userId")).thenReturn(null);
        when(prizeRepository.findAllWonPrizes()).thenReturn(Collections.emptyList());

        String viewName = userService.openWinnersPage(model, session);

        verify(model).addAttribute("loggedIn", false);
        verify(model).addAttribute("wonPrizes", Collections.emptyList());
        assertEquals("winners", viewName);
    }

    @Test
    public void testOpenAboutPageWhenLoggedInIsNull() {
        when(session.getAttribute("loggedIn")).thenReturn(null);
        when(session.getAttribute("userId")).thenReturn(null);

        String viewName = userService.openAboutPage(model, session);

        verify(session).setAttribute("loggedIn", false);
        verify(model).addAttribute("loggedIn", false);
        verifyNoMoreInteractions(model);
        assertEquals("about", viewName);
    }

    @Test
    public void testOpenAboutPageWhenUserIdIsNull() {
        when(session.getAttribute("loggedIn")).thenReturn(true);
        when(session.getAttribute("userId")).thenReturn(null);

        String viewName = userService.openAboutPage(model, session);

        verify(session).setAttribute("loggedIn", true);
        verify(model).addAttribute("loggedIn", true);
        verifyNoMoreInteractions(model);
        assertEquals("about", viewName);
    }

    @Test
    public void testOpenAboutPageWhenUserIdIsNotNullAndUserIsPresent() {
        Long userId = 1L;
        User user = new User();
        user.setTotalPoints(100);

        when(session.getAttribute("loggedIn")).thenReturn(true);
        when(session.getAttribute("userId")).thenReturn(userId);
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        String viewName = userService.openAboutPage(model, session);

        verify(model).addAttribute("loggedUser", user);
        verify(model).addAttribute("totalPoints", 100);
        verify(session).setAttribute("loggedUser", user);
        assertEquals("about", viewName);
    }

    @Test
    public void testOpenUsersPageWhenUserIdIsNull() {
        when(session.getAttribute("userId")).thenReturn(null);

        String viewName = userService.openUsersPage(model, session);

        assertEquals("redirect:/home", viewName);
    }

    @Test
    public void testOpenUsersPageWhenUserNotFound() {
        Long userId = 1L;

        when(session.getAttribute("userId")).thenReturn(userId);
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        String viewName = userService.openUsersPage(model, session);

        assertEquals("redirect:/home", viewName);
    }

    @Test
    public void testOpenUsersPageWhenUserRoleIsAdmin() {
        Long userId = 1L;
        User user = new User();
        user.setRole(Role.ADMIN);
        user.setTotalPoints(100);
        when(session.getAttribute("userId")).thenReturn(userId);
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        String viewName = userService.openUsersPage(model, session);

        verify(model).addAttribute("totalPoints", 100);
        verify(model).addAttribute("loggedUser", user);
        verify(model).addAttribute("users", Collections.emptyList());
        assertEquals("admin_users", viewName);
    }

    @Test
    public void testOpenUsersPageWhenUserRoleIsNotAdmin() {
        Long userId = 1L;
        User user = new User();
        user.setRole(Role.CLIENT);

        when(session.getAttribute("userId")).thenReturn(userId);
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        String viewName = userService.openUsersPage(model, session);

        assertEquals("redirect:/home", viewName);
    }


    @Test
    public void testOpenUserAddressWhenUserIsNotFound() {
        Long userCode = 12345L;

        when(userService.getUserByUserCode(userCode)).thenReturn(null);

        ResponseEntity<String> response = userService.openUserAddress(userCode);

        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    public void testOpenUserAddressWhenAddressIsNotFound() {
        Long userCode = 12345L;
        User user = new User();

        when(userService.getUserByUserCode(userCode)).thenReturn(user);

        ResponseEntity<String> response = userService.openUserAddress(userCode);

        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    public void testOpenUserAddressWhenExceptionOccurs() {
        Long userCode = 12345L;
        when(userService.getUserByUserCode(userCode)).thenThrow(new RuntimeException("Test Exception"));

        ResponseEntity<String> response = userService.openUserAddress(userCode);

        assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Възникна грешка: Test Exception"), response);
    }

    @Test
    @Transactional
    public void testDeleteUserByCodeProcessWhenUserFound() {
        // Arrange
        Long userCode = 12345L;
        User user = new User();
        when(userRepository.findByUserCode(userCode)).thenReturn(user);

        String result = userService.deleteUserByCodeProcess(userCode);

        verify(userRepository).save(user);
        assertEquals("Потребителят е изтрит успешно!", result);
    }

    @Test
    public void testDeleteUserByCodeProcessWhenUserNotFound() {
        Long userCode = 12345L;
        when(userRepository.findByUserCode(userCode)).thenReturn(null);

        String result = userService.deleteUserByCodeProcess(userCode);

        assertEquals("Потребител с този код не съществува!", result);
    }

    @Test
    public void testOpenEditUserFormWhenUserFound() {
        Long userId = 1L;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        String viewName = userService.openEditUserForm(userId, model);

        verify(model).addAttribute("user", Optional.of(user));
        assertEquals("edit-user", viewName);
    }

    @Test
    public void testOpenEditUserFormWhenUserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        String viewName = userService.openEditUserForm(userId, model);

        verify(model).addAttribute("user", Optional.empty());
        assertEquals("edit-user", viewName);
    }

    @Test
    public void testActivateAccountProcessWhenTokenIsValid() {
        String token = "validToken";
        User user = new User();
        user.setTokenExpiry(LocalDateTime.now().plusHours(1));
        when(userRepository.findByActivationToken(token)).thenReturn(user);

        String viewName = userService.activateAccountProcess(token, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("success", "Профилът е активиран успешно!");
        assertEquals("redirect:/activated_profile", viewName);
    }

    @Test
    public void testActivateAccountProcessWhenTokenIsExpired() {
        String token = "expiredToken";
        User user = new User();
        user.setTokenExpiry(LocalDateTime.now().minusHours(1));
        when(userRepository.findByActivationToken(token)).thenReturn(user);

        String viewName = userService.activateAccountProcess(token, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("linkExpired", "Линкът е изтекъл! Изминали са повече от 24 часа от получаването на този email.");
        assertEquals("redirect:/activated_profile", viewName);
    }

    @Test
    public void testActivateAccountProcessWhenTokenIsInvalid() {
        String token = "invalidToken";
        when(userRepository.findByActivationToken(token)).thenReturn(null);

        String viewName = userService.activateAccountProcess(token, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("error", "Вече сте активирали своя профил!");
        assertEquals("redirect:/activated_profile", viewName);
    }

    // --------------------------

    @Test
    public void testResetPasswordProcessWhenUserAndTokenAreValid() {
        String token = "validToken";
        User user = new User();
        user.setTokenExpiry(LocalDateTime.now().plusHours(1));
        when(userService.findByResetToken(token)).thenReturn(user);

        String viewName = userService.resetPasswordProcess(token, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("token", token);
        assertEquals("redirect:/show_reset_password_form", viewName);
    }

    @Test
    public void testResetPasswordProcessWhenTokenIsExpired() {
        String token = "expiredToken";
        User user = new User();
        user.setTokenExpiry(LocalDateTime.now().minusHours(1));
        when(userService.findByResetToken(token)).thenReturn(user);

        String viewName = userService.resetPasswordProcess(token, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("error", "Линкът за възстановяване на парола е невалиден или е изтекъл.");
        assertEquals("redirect:/login", viewName);
    }

    @Test
    public void testResetPasswordProcessWhenUserIsNotFound() {
        String token = "invalidToken";
        when(userService.findByResetToken(token)).thenReturn(null);

        String viewName = userService.resetPasswordProcess(token, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("error", "Линкът за възстановяване на парола е невалиден или е изтекъл.");
        assertEquals("redirect:/login", viewName);
    }

    @Test
    public void testRegisterProcessWhenPasswordsDoNotMatch() {
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String password = "password1";
        String confirmPassword = "password2";
        String ageConfirmation = "yes";

        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        String viewName = userService.registerProcess(firstName, lastName, email, password, confirmPassword, ageConfirmation, redirectAttributes, null);

        verify(redirectAttributes).addFlashAttribute("error", "Паролите не съвпадат.");
        verify(redirectAttributes).addFlashAttribute("firstName", firstName);
        verify(redirectAttributes).addFlashAttribute("lastName", lastName);
        verify(redirectAttributes).addFlashAttribute("email", email);
        assertEquals("redirect:/register", viewName);
    }

    @Test
    public void testRegisterProcessWhenAgeConfirmationIsNo() {
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String password = "password";
        String confirmPassword = "password";
        String ageConfirmation = "no";

        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        String viewName = userService.registerProcess(firstName, lastName, email, password, confirmPassword, ageConfirmation, redirectAttributes, null);

        verify(redirectAttributes).addFlashAttribute("error", "Трябва да потвърдите, че имате навършени 18 години. Ако не сте пълнолетен, не можете да се регистрирате.");
        verify(redirectAttributes).addFlashAttribute("firstName", firstName);
        verify(redirectAttributes).addFlashAttribute("lastName", lastName);
        verify(redirectAttributes).addFlashAttribute("email", email);
        assertEquals("redirect:/register", viewName);
    }

    @Test
    public void testRegisterProcessWhenEmailAlreadyExists() {
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String password = "password";
        String confirmPassword = "password";
        String ageConfirmation = "yes";

        User existingUser = new User();
        when(userService.findByEmail(email)).thenReturn(existingUser);

        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        String viewName = userService.registerProcess(firstName, lastName, email, password, confirmPassword, ageConfirmation, redirectAttributes, null);

        verify(redirectAttributes).addFlashAttribute("error", "Вече има регистриран потребител с този email адрес.");
        verify(redirectAttributes).addFlashAttribute("firstName", firstName);
        verify(redirectAttributes).addFlashAttribute("lastName", lastName);
        verify(redirectAttributes).addFlashAttribute("email", email);
        assertEquals("redirect:/register", viewName);
    }

//    @Test
//    public void testRegisterProcessWhenSuccessful() {
//        String firstName = "John";
//        String lastName = "Doe";
//        String email = "john.doe@example.com";
//        String password = "password";
//        String confirmPassword = "password";
//        String ageConfirmation = "yes";
//
//        when(userService.findByEmail(email)).thenReturn(null);
//        when(addressService.saveAddress(any(Address.class))).thenReturn(new Address());
//        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
//        when(userService.generateUniqueUserCode()).thenReturn(12345L);
//
//        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
//        Model model = mock(Model.class);
//        String viewName = userService.registerProcess(firstName, lastName, email, password, confirmPassword, ageConfirmation, redirectAttributes, model);
//
//        verify(userService).saveUser(any(User.class));
//        verify(emailService).sendActivationEmail(any(User.class), anyString());
//        verify(emailConfiguration).printEmailSettings();
//        verify(model).addAttribute("success", "За да активирате своя профил, в следващите 24 часа трябва да натиснете върху линка, изпратен на Вашия email адрес.");
//        assertEquals("register", viewName);
//    }

//    @Test
//    public void testLoginProcessWhenUserIsActivated() {
//        String email = "john.doe@example.com";
//        String password = "password";
//        User user = new User();
//        user.setId(1L);
//        user.setRole(Role.CLIENT);
//        user.setTotalPoints(100);
//        when(userService.getUserByEmail(email)).thenReturn(user);
//        when(userService.verifyPassword(user, password)).thenReturn(true);
//
//        String viewName = userService.loginProcess(email, password, model, session);
//
//        verify(session).setAttribute("userId", 1L);
//        verify(session).setAttribute("loggedIn", true);
//        verify(session).setAttribute("userRole", "USER");
//        verify(session).setAttribute("totalPoints", 100);
//        assertEquals("redirect:/home", viewName);
//    }

    @Test
    public void testLoginProcessWhenUserIsNotActivated() {
        String email = "john.doe@example.com";
        String password = "password";
        User user = new User();
        user.setId(1L);
        user.setRole(Role.CLIENT);
        user.setTotalPoints(100);
        user.setActivated(false);
        when(userService.getUserByEmail(email)).thenReturn(user);
        when(userService.verifyPassword(user, password)).thenReturn(true);

        String viewName = userService.loginProcess(email, password, model, session);

        verify(model).addAttribute("errorNotActivated", true);
        verify(model).addAttribute("error", false);
        assertEquals("login", viewName);
    }

    @Test
    public void testLoginProcessWhenInvalidCredentials() {
        String email = "john.doe@example.com";
        String password = "wrongPassword";
        when(userService.getUserByEmail(email)).thenReturn(null);

        String viewName = userService.loginProcess(email, password, model, session);

        verify(model).addAttribute("error", true);
        verify(model).addAttribute("errorNotActivated", false);
        assertEquals("login", viewName);
    }

    @Test
    public void testForgotPasswordProcessWhenEmailNotFound() {
        String email = "unknown@example.com";
        when(userService.getUserByEmail(email)).thenReturn(null);

        String viewName = userService.forgotPasswordProcess(email, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("error", "Невалиден email адрес!");
        assertEquals("redirect:/login", viewName);
    }

//    @Test
//    public void testForgotPasswordProcessWhenSuccessful() throws Exception {
//        String email = "john.doe@example.com";
//        User user = new User();
//        when(userService.getUserByEmail(email)).thenReturn(user);
//        when(userService.saveUser(any(User.class))).thenReturn(user);
//
//        String token = UUID.randomUUID().toString();
//        when(userService.findByResetToken(anyString())).thenReturn(user);
//
//        String viewName = userService.forgotPasswordProcess(email, redirectAttributes);
//
//        verify(redirectAttributes).addFlashAttribute("message", "Линкът за възстановяване на парола е изпратен на вашия email!");
//        assertEquals("redirect:/login", viewName);
//    }
//
//    @Test
//    public void testForgotPasswordProcessWhenEmailSendingFails() throws Exception {
//        String email = "john.doe@example.com";
//        User user = new User();
//        when(userService.getUserByEmail(email)).thenReturn(user);
//        doThrow(new RuntimeException("Email sending failed")).when(emailService).sendHtmlEmail(user, anyString());
//
//        String viewName = userService.forgotPasswordProcess(email, redirectAttributes);
//
//        verify(redirectAttributes).addFlashAttribute("error", "Възникна грешка при изпращането на email.");
//        assertEquals("redirect:/login", viewName);
//    }


//    @Test
//    public void testHandlePasswordResetProcessWhenTokenIsValid() {
//        String token = "validToken";
//        String password = "newPassword";
//        User user = new User();
//        user.setTokenExpiry(LocalDateTime.now().plusHours(1));
//        when(userService.findByResetToken(token)).thenReturn(user);
//        when(userService.saveUser(any(User.class))).thenReturn(user);
//
//        String viewName = userService.handlePasswordResetProcess(token, password, redirectAttributes);
//
//        verify(userService).saveUser(any(User.class));
//        verify(redirectAttributes).addFlashAttribute("message", "Паролата е възстановена успешно!");
//        assertEquals("redirect:/login", viewName);
//    }

    @Test
    public void testHandlePasswordResetProcessWhenTokenIsExpired() {
        String token = "expiredToken";
        String password = "newPassword";
        User user = new User();
        user.setTokenExpiry(LocalDateTime.now().minusHours(1));
        when(userService.findByResetToken(token)).thenReturn(user);

        String viewName = userService.handlePasswordResetProcess(token, password, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("error", "Линкът за възстановяване на парола е невалиден или е изтекъл.");
        assertEquals("redirect:/login", viewName);
    }

    @Test
    public void testHandlePasswordResetProcessWhenTokenIsInvalid() {
        String token = "invalidToken";
        String password = "newPassword";
        when(userService.findByResetToken(token)).thenReturn(null);

        String viewName = userService.handlePasswordResetProcess(token, password, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("error", "Линкът за възстановяване на парола е невалиден или е изтекъл.");
        assertEquals("redirect:/login", viewName);
    }


    @Test
    public void testUpdateProfileProcessWhenUserIdIsNull() {
        when(session.getAttribute("userId")).thenReturn(null);

        ResponseEntity<String> response = userService.updateProfileProcess("John", "Doe", "john@example.com", 123456789, "City", 1000, "Street", 10, 1, 1, session);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Потребителят не е оторизиран!", response.getBody());
    }

    @Test
    public void testUpdateProfileProcessWhenRequiredFieldsAreEmpty() {
        when(session.getAttribute("userId")).thenReturn(1L);

        ResponseEntity<String> response = userService.updateProfileProcess("", "", "", null, "", null, "", null, null, null, session);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Моля, попълнете всички полета, маркирани със звездичка, за да актуализирате профила си!", response.getBody());
    }

    @Test
    public void testUpdateProfileProcessWhenUserNotFound() {
        when(session.getAttribute("userId")).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = userService.updateProfileProcess("John", "Doe", "john@example.com", 123456789, "City", 1000, "Street", 10, 1, 1, session);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Потребителят не е намерен!", response.getBody());
    }

    @Test
    public void testUpdateProfileProcessWhenSuccessful() {
        when(session.getAttribute("userId")).thenReturn(1L);
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<String> response = userService.updateProfileProcess("John", "Doe", "john@example.com", 123456789, "City", 1000, "Street", 10, 1, 1, session);

        verify(userRepository).save(user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Данните са актуализирани успешно!", response.getBody());
    }


//    @Test
//    public void testUpdateUserProcess() {
//        Long userCode = 1L;
//        User existingUser = new User();
//        User updatedUser = new User();
//        updatedUser.setFirstName("John");
//        updatedUser.setLastName("Doe");
//        updatedUser.setEmail("john.doe@example.com");
//        updatedUser.setPhone(123456789);
//        updatedUser.setRole(Role.CLIENT);
//
//        when(userRepository.findById(userCode)).thenReturn(Optional.of(existingUser));
//
//        String viewName = userService.updateUserProcess(userCode, updatedUser);
//
//        verify(userRepository).save(existingUser);
//        assertEquals("redirect:/users", viewName);
//    }


    @Test
    public void testLogoutProcess() {
        String viewName = userService.logoutProcess(session, response);

        verify(session).removeAttribute("userId");
        verify(session).invalidate();
        verify(response).setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        assertEquals("redirect:/home", viewName);
    }



}