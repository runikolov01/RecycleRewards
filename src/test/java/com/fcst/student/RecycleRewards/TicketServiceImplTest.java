package com.fcst.student.RecycleRewards;

import com.fcst.student.RecycleRewards.model.Ticket;
import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.repository.TicketRepository;
import com.fcst.student.RecycleRewards.service.UserService;
import com.fcst.student.RecycleRewards.service.impl.TicketServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private User loggedInUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        loggedInUser = new User();
        loggedInUser.setId(1L);
        loggedInUser.setTotalPoints(50);
        when(session.getAttribute("userId")).thenReturn(loggedInUser.getId());
        when(userService.getUserById(loggedInUser.getId())).thenReturn(Optional.of(loggedInUser));
    }

    @Test
    public void testGetDefaultBottlesCount() {
        assertEquals(0, ticketService.getDefaultBottlesCount(null));
        assertEquals(5, ticketService.getDefaultBottlesCount(5));
    }

    @Test
    public void testOpenStartPage() {
        when(session.getAttribute("bottlesCount")).thenReturn(5);
        when(session.getAttribute("isVoucher")).thenReturn(true);

        String viewName = ticketService.openStartPage(session, model);

        assertEquals("start", viewName);
        verify(model).addAttribute("bottlesCount", 5);
        verify(model).addAttribute("isVoucher", true);
    }

//    @Test
//    public void testOpenPrintPage_NewTicket() {
//        when(session.getAttribute("bottlesCount")).thenReturn(10);
//        when(session.getAttribute("ticketNumber")).thenReturn(null);
//        when(session.getAttribute("isVoucher")).thenReturn(false);
//
//        String viewName = ticketService.openPrintPage(session, model);
//
//        assertEquals("print", viewName);
//        verify(session).setAttribute(eq("ticketNumber"), anyString());
//        verify(model).addAttribute(eq("ticketNumber"), anyString());
//        verify(model).addAttribute(eq("issuedOnFormatted"), anyString());
//        verify(model).addAttribute(eq("expirationFormatted"), anyString());
//        verify(model).addAttribute("bottlesCount", 10);
//        verify(model).addAttribute("isVoucher", false);
//    }

    @Test
    public void testOpenPrintPage_ExistingTicket() {
        when(session.getAttribute("bottlesCount")).thenReturn(10);
        when(session.getAttribute("ticketNumber")).thenReturn("EXIST123");
        when(session.getAttribute("isVoucher")).thenReturn(false);

        String viewName = ticketService.openPrintPage(session, model);

        assertEquals("print", viewName);
        verify(model).addAttribute("ticketNumber", "EXIST123");
        verify(model).addAttribute(eq("issuedOnFormatted"), anyString());
        verify(model).addAttribute(eq("expirationFormatted"), anyString());
        verify(model).addAttribute("bottlesCount", 10);
        verify(model).addAttribute("isVoucher", false);
    }

    @Test
    public void testOpenRegisterTicketPage_LoggedIn() {
        when(session.getAttribute("loggedIn")).thenReturn(true);
        when(session.getAttribute("userId")).thenReturn(loggedInUser.getId());
        when(userService.getUserById(loggedInUser.getId())).thenReturn(Optional.of(loggedInUser));

        String viewName = ticketService.openRegisterTicketPage(session, model);

        assertEquals("registerTicket", viewName);
        verify(model).addAttribute("loggedIn", true);
        verify(model).addAttribute("loggedUser", loggedInUser);
        verify(model).addAttribute("totalPoints", loggedInUser.getTotalPoints());
        verify(session).setAttribute("loggedUser", loggedInUser);
        verify(session).setAttribute("totalPoints", loggedInUser.getTotalPoints());
    }

    @Test
    public void testOpenRegisterTicketPage_NotLoggedIn() {
        when(session.getAttribute("loggedIn")).thenReturn(null);
        when(session.getAttribute("userId")).thenReturn(null);

        String viewName = ticketService.openRegisterTicketPage(session, model);

        assertEquals("registerTicket", viewName);
        verify(model).addAttribute("loggedIn", false);
    }

    @Test
    public void testSetVoucherAttribute_True() {
        Map<String, Boolean> requestBody = new HashMap<>();
        requestBody.put("isVoucher", true);

        ResponseEntity<Map<String, Object>> response = ticketService.setVoucherAttribute(requestBody, session);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().get("success").equals(true));
        verify(session).setAttribute("isVoucher", true);
    }

    @Test
    public void testSetVoucherAttribute_False() {
        Map<String, Boolean> requestBody = new HashMap<>();
        requestBody.put("isVoucher", false);

        ResponseEntity<Map<String, Object>> response = ticketService.setVoucherAttribute(requestBody, session);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().get("success").equals(true));
        verify(session).setAttribute("isVoucher", false);
    }

    @Test
    public void testAddBottle() {
        when(session.getAttribute("bottlesCount")).thenReturn(5);

        ticketService.addBottle(session);

        verify(session).setAttribute("bottlesCount", 6);
    }

    @Test
    public void testExitSession() {
        when(session.getAttribute("bottlesCount")).thenReturn(10);
        when(session.getAttribute("ticketNumber")).thenReturn("ABC12345");
        when(session.getAttribute("isVoucher")).thenReturn(false);

        String viewName = ticketService.exitSession(session, model);

        assertEquals("print_with_alert", viewName);
        verify(session).invalidate();
        verify(model).addAttribute("alertMessage", "Printing...");
    }

    @Test
    public void testRegisterTicket_Success() {
        Ticket ticket = new Ticket();
        ticket.setIssued(LocalDateTime.now());
        ticket.setVoucher(false);
        when(ticketRepository.findByNumber("ABC12345")).thenReturn(Optional.of(ticket));

        ResponseEntity<String> response = ticketService.registerTicket("ABC12345", session, model);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("0 точки са добавени успешно към Вашия профил", response.getBody());
        verify(ticketRepository).save(ticket);
        verify(userService).saveUser(loggedInUser);
    }

    @Test
    public void testRegisterTicket_AlreadyRegistered() {
        Ticket ticket = new Ticket();
        ticket.setIssued(LocalDateTime.now());
        ticket.setVoucher(false);
        ticket.setRegisteredOn(LocalDateTime.now());
        when(ticketRepository.findByNumber("ABC12345")).thenReturn(Optional.of(ticket));

        ResponseEntity<String> response = ticketService.registerTicket("ABC12345", session, model);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Кодът е вече регистриран. Моля опитайте с друг код.", response.getBody());
    }

    @Test
    public void testRegisterTicket_TicketExpired() {
        Ticket ticket = new Ticket();
        ticket.setIssued(LocalDateTime.now().minusHours(100));
        ticket.setVoucher(false);
        when(ticketRepository.findByNumber("ABC12345")).thenReturn(Optional.of(ticket));

        ResponseEntity<String> response = ticketService.registerTicket("ABC12345", session, model);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Времето за регистрация на този билет е изтекло. Моля, опитайте с друг билет.", response.getBody());
    }

    @Test
    public void testRegisterTicket_NotFound() {
        when(ticketRepository.findByNumber("ABC12345")).thenReturn(Optional.empty());

        ResponseEntity<String> response = ticketService.registerTicket("ABC12345", session, model);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Билетът не беше намерен. Моля, въведете валиден номер.", response.getBody());
    }

    @Test
    public void testRegisterTicket_NotLoggedIn() {
        when(session.getAttribute("userId")).thenReturn(null);

        ResponseEntity<String> response = ticketService.registerTicket("ABC12345", session, model);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Не сте оторизиран за тази операция. Моля, влезте в профила си и опитайте отново.", response.getBody());
    }

    @Test
    public void testGenerateUniqueTicketNumber() {
        when(ticketRepository.existsByNumber(anyString())).thenReturn(false);

        String ticketNumber = ticketService.generateUniqueTicketNumber();

        assertNotNull(ticketNumber);
        assertEquals(8, ticketNumber.length());
    }
}