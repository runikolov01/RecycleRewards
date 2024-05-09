package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.Ticket;
import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.repository.TicketRepository;
import com.fcst.student.RecycleRewards.service.TicketService;
import com.fcst.student.RecycleRewards.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.format.DateTimeFormatter;

@Controller
public class PagesController {
    private final UserService userService;
    private final TicketRepository ticketRepository;
    @Autowired
    private TicketService ticketService;

    @Autowired
    public PagesController(UserService userService, TicketRepository ticketRepository) {
        this.userService = userService;
        this.ticketRepository = ticketRepository;
    }

    private void setAttributesAndModel(HttpSession session, Model model, Ticket ticket, Integer bottlesCount) {
        String issuedOnFormatted = ticket.getIssued().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));
        String expirationFormatted = ticket.getActiveUntil().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));

        session.setAttribute("ticketNumber", ticket.getNumber());
        session.setAttribute("issuedOnFormatted", issuedOnFormatted);
        session.setAttribute("expirationFormatted", expirationFormatted);

        model.addAttribute("bottlesCount", bottlesCount);
        model.addAttribute("ticketNumber", ticket.getNumber());
        model.addAttribute("issuedOnFormatted", issuedOnFormatted);
        model.addAttribute("expirationFormatted", expirationFormatted);
    }

    @GetMapping("/index")
    public String openHomePage() {
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        // Check if the user is logged in
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean loggedIn = authentication != null && authentication.isAuthenticated();

        // Add the loggedIn attribute to the model
        model.addAttribute("loggedIn", loggedIn);

        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            User user = userService.getUserById(userId);
            if (user != null) {
                model.addAttribute("loggedUser", user);

                // Fetch totalPoints for the logged-in user and add it to the model
                Integer totalPoints = user.getTotalPoints();
                model.addAttribute("totalPoints", totalPoints);
            }
            System.out.println(user.getRole());
        }
        return "home";
    }

    @GetMapping("/start")
    public String openStartPage(HttpSession session, Model model) {
        Integer bottlesCount = (Integer) session.getAttribute("bottlesCount");
        bottlesCount = ticketService.getDefaultBottlesCount(bottlesCount);

        model.addAttribute("bottlesCount", bottlesCount);
        return "start";
    }

    @GetMapping("/about")
    public String about(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            // Fetch user details from the database using the user ID
            User user = userService.getUserById(userId);
            if (user != null) {
                model.addAttribute("loggedUser", user);
                Integer totalPoints = user.getTotalPoints();
                model.addAttribute("totalPoints", totalPoints);
            }
        }
        return "about";
    }

    @GetMapping("/print")
    public String openPrintPage(HttpSession session, Model model) {
        Integer bottlesCount = (Integer) session.getAttribute("bottlesCount");
        String ticketNumber = (String) session.getAttribute("ticketNumber");
        bottlesCount = ticketService.getDefaultBottlesCount(bottlesCount);

        if (ticketNumber == null) {
            ticketNumber = ticketService.generateUniqueTicketNumber(); // Generate a new ticket number if not already generated
            session.setAttribute("ticketNumber", ticketNumber); // Store it in the session
        }

        Ticket ticket = ticketService.createTicket(bottlesCount, ticketNumber);
        setAttributesAndModel(session, model, ticket, bottlesCount);

        return "print";
    }

    @PostMapping("/print")
    public String exitSession(HttpSession session, Model model) {
        Integer bottlesCount = (Integer) session.getAttribute("bottlesCount");
        bottlesCount = ticketService.getDefaultBottlesCount(bottlesCount);
        String ticketNumber = (String) session.getAttribute("ticketNumber");

        Ticket ticket = ticketService.createTicket(bottlesCount, ticketNumber);
        setAttributesAndModel(session, model, ticket, bottlesCount);

        ticketService.saveTicket(ticket);
        session.setAttribute("bottlesCount", 0);
        session.invalidate();

        // Passing the alert message as a parameter
        model.addAttribute("alertMessage", "Printing...");

        return "print_with_alert";
    }

    @PostMapping("/start")
    public String addBottle(HttpSession session) {
        Integer bottlesCount = (Integer) session.getAttribute("bottlesCount");
        bottlesCount = ticketService.getDefaultBottlesCount(bottlesCount);
        bottlesCount++;

        session.setAttribute("bottlesCount", bottlesCount);

        return "redirect:/start";
    }

}
