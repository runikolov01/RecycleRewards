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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

@Controller
public class PrintController {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketService ticketService;

    @GetMapping("/print")
    public String openPrintPage(HttpSession session, Model model) {
        Integer bottlesCount = (Integer) session.getAttribute("bottlesCount");

        bottlesCount = ticketService.getDefaultBottlesCount(bottlesCount);

        Ticket ticket = ticketService.createTicket(bottlesCount);

        String issuedOnFormatted = ticket.getIssued().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));
        String expirationFormatted = ticket.getActiveUntil().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));

        session.setAttribute("ticketNumber", ticket.getNumber());
        session.setAttribute("issuedOnFormatted", issuedOnFormatted);
        session.setAttribute("expirationFormatted", expirationFormatted);

        model.addAttribute("bottlesCount", bottlesCount);
        model.addAttribute("ticketNumber", ticket.getNumber());
        model.addAttribute("issuedOnFormatted", issuedOnFormatted);
        model.addAttribute("expirationFormatted", expirationFormatted);

        return "print";
    }

    @PostMapping("/print")
    public String exitSession(HttpSession session) {
        session.setAttribute("bottlesCount", 0);
        session.invalidate();
        return "redirect:/index";
    }
}