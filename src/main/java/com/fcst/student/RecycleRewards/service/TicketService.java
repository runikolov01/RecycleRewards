package com.fcst.student.RecycleRewards.service;

import com.fcst.student.RecycleRewards.model.Ticket;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Map;

@Service
public interface TicketService {
    String openStartPage(HttpSession session, Model model);

    String openPrintPage(HttpSession session, Model model);

    String openRegisterTicketPage(HttpSession session, Model model);

    ResponseEntity<Map<String, Object>> setVoucherAttribute(Map<String, Boolean> requestBody, HttpSession session);

    void addBottle(HttpSession session);

    String exitSession(HttpSession session, Model model);

    ResponseEntity<String> registerTicket(String ticketNumber, HttpSession session, Model model);

    Integer getDefaultBottlesCount(Integer bottlesCount);

    Ticket createTicket(Integer bottlesCount, String ticketNumber, Boolean isVoucher);

    void saveTicket(Ticket ticket);

    String generateUniqueTicketNumber();
}