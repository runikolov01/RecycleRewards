package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.Ticket;
import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.repository.TicketRepository;
import com.fcst.student.RecycleRewards.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class TicketController {

    private final TicketRepository ticketRepository;
    private final UserService userService;

    public TicketController(TicketRepository ticketRepository, UserService userService) {
        this.ticketRepository = ticketRepository;
        this.userService = userService;
    }

    @GetMapping("/registerTicket")
    public String registerTicket() {
        return "registerTicket";
    }

    @PostMapping("/registerTicket")
    public ResponseEntity<String> registerTicket(@RequestParam("ticketNumber") String ticketNumber,
                                                 HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            // Fetch user details from the database using the user ID
            User currentUser = userService.getUserById(userId);
            if (currentUser != null) {
                Ticket ticket = ticketRepository.findByNumber(ticketNumber);
                if (ticket != null) {
                    // Check if the ticket is already registered
                    if (ticket.getRegisteredOn() != null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Кодът е вече регистриран. Моля опитайте с друг код.");
                    }
                    // Update the ticket
                    ticket.setRegisteredOn(LocalDateTime.now());
                    ticket.setRegisteredBy(currentUser);
                    ticketRepository.save(ticket);
                    return ResponseEntity.ok(ticket.getPoints() + " точки са добавени успешно към Вашия профил");
                } else {
                    // Return a response indicating the ticket is not found
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Билетът не беше намерен. Моля, въведете валиден номер.");
                }
            }
        }
        // Handle case when user ID is not found or user is not found
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Не сте оторизиран за тази операция. Моля, влезте в профила си и опитайте отново.");
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String email = authentication.getName(); // Assuming email is used as the principal
            return userService.getUserByEmail(email); // Assuming you have a method to find a user by email
        } else {
            // Handle the case where there is no authenticated user
            // You might want to return a default user or throw an exception
            // Here, we'll return null for simplicity, but adjust this according to your application's logic
            return null;
        }
    }


}