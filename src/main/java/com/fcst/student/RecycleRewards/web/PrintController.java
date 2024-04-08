package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.Ticket;
import com.fcst.student.RecycleRewards.repository.TicketRepository;
import com.fcst.student.RecycleRewards.service.TicketService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.format.DateTimeFormatter;

@Controller
public class PrintController {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketService ticketService;

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
        String ticketNumber = (String) session.getAttribute("ticketNumber"); // Retrieve the ticket number from the session

        Ticket ticket = ticketService.createTicket(bottlesCount, ticketNumber); // Use the same ticket number
        setAttributesAndModel(session, model, ticket, bottlesCount);

        ticketService.saveTicket(ticket);
        session.setAttribute("bottlesCount", 0);
        session.invalidate();

        // Passing the alert message as a parameter
        model.addAttribute("alertMessage", "Printing...");

        return "print_with_alert";
    }
}