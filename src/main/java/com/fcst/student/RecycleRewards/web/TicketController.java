package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.Ticket;
import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.repository.TicketRepository;
import com.fcst.student.RecycleRewards.service.UserService;
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
    public ResponseEntity<String> registerTicket(@RequestParam("ticketNumber") String ticketNumber) {
        Ticket ticket = ticketRepository.findByNumber(ticketNumber);
        if (ticket != null) {
            // Your existing code
            // Update the ticket
            ticket.setRegisteredOn(LocalDateTime.now());
            // Set the currently logged-in user
//            User currentUser = getCurrentUser();
//            ticket.setRegisteredBy(currentUser);
            ticketRepository.save(ticket);
            return ResponseEntity.ok(ticket.getPoints() + " точки са добавени успешно към Вашия профил");
        } else {
            // Return a response indicating the ticket is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Билетът не беше намерен. Моля, въведете валиден номер.");
        }
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Assuming email is used as the principal
        return userService.getUserByEmail(email); // Assuming you have a method to find a user by email
    }

}
