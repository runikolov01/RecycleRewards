package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.service.TicketService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @GetMapping("/index")
    public String openHomePage() {
        return "index";
    }

    @GetMapping("/start")
    public String openStartPage(HttpSession session, Model model) {
        return ticketService.openStartPage(session, model);
    }

    @GetMapping("/print")
    public String openPrintPage(HttpSession session, Model model) {
        return ticketService.openPrintPage(session, model);
    }

    @GetMapping("/registerTicket")
    public String registerTicket(HttpSession session, Model model) {
        return ticketService.openRegisterTicketPage(session, model);
    }

    @PostMapping("/setVoucherAttribute")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> setVoucherAttribute(@RequestBody Map<String, Boolean> requestBody, HttpSession session) {
        return ticketService.setVoucherAttribute(requestBody, session);
    }

    @PostMapping("/start")
    public String addBottle(HttpSession session) {
        ticketService.addBottle(session);
        return "redirect:/start";
    }

    @PostMapping("/print")
    public String exitSession(HttpSession session, Model model) {
        return ticketService.exitSession(session, model);
    }

    @PostMapping("/registerTicket")
    public ResponseEntity<String> registerTicket(@RequestParam("ticketNumber") String ticketNumber, HttpSession session, Model model) {
        return ticketService.registerTicket(ticketNumber, session, model);
    }
}