package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.Ticket;
import com.fcst.student.RecycleRewards.repository.TicketRepository;
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

    @GetMapping("/print")
    public String openPrintPage(HttpSession session, Model model) {
        Integer bottlesCount = (Integer) session.getAttribute("bottlesCount");
        if (bottlesCount == null) {
            bottlesCount = 0;
        }
        String ticketNumber = generateUniqueTicketNumber();

        LocalDateTime issuedOn = LocalDateTime.now();
        LocalDateTime expirationDateTime = issuedOn.plusHours(72);


        String issuedOnFormatted = issuedOn.format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));
        String expirationFormatted = expirationDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));

        session.setAttribute("ticketNumber", ticketNumber);
        session.setAttribute("issuedOnFormatted", issuedOnFormatted);
        session.setAttribute("expirationFormatted", expirationFormatted);

        model.addAttribute("bottlesCount", bottlesCount);
        model.addAttribute("ticketNumber", ticketNumber);
        model.addAttribute("issuedOnFormatted", issuedOnFormatted);
        model.addAttribute("expirationFormatted", expirationFormatted);

        return "print";
    }

    @PostMapping("/print")
    public String exitSession(HttpSession session) {
        Integer bottlesCount = (Integer) session.getAttribute("bottlesCount");
        String ticketNumber = (String) session.getAttribute("ticketNumber");
        String issuedOnFormatted = (String) session.getAttribute("issuedOnFormatted");
        String expirationFormatted = (String) session.getAttribute("expirationFormatted");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
        LocalDateTime issuedOnDateTime = LocalDateTime.parse(issuedOnFormatted, formatter);
        LocalDateTime expirationDateTime = LocalDateTime.parse(expirationFormatted, formatter);

        Ticket ticket = new Ticket();
        ticket.setNumber(ticketNumber);
        ticket.setIssued(issuedOnDateTime);
        ticket.setActiveUntil(expirationDateTime);
        ticket.setPoints(bottlesCount);

        ticketRepository.save(ticket);

        session.setAttribute("bottlesCount", 0);
        session.invalidate();

        return "redirect:/index";
    }



    private String generateUniqueTicketNumber() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder ticketNumberBuilder = new StringBuilder();
        Random random = new Random();
        int length = 8;

        while (true) {
            for (int i = 0; i < length; i++) {
                ticketNumberBuilder.append(alphabet.charAt(random.nextInt(alphabet.length())));
            }

            String ticketNumber = ticketNumberBuilder.toString();

            if (!ticketNumberExistsInDatabase(ticketNumber)) {
                return ticketNumber;
            }
            ticketNumberBuilder.setLength(0);
        }
    }

    private boolean ticketNumberExistsInDatabase(String ticketNumber) {
        return ticketRepository.existsByNumber(ticketNumber);
    }
}