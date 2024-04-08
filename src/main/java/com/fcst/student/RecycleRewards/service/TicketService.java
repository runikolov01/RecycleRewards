package com.fcst.student.RecycleRewards.service;

import com.fcst.student.RecycleRewards.model.Ticket;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface TicketService {
    Integer getDefaultBottlesCount(Integer bottlesCount);
    Ticket createTicket(Integer bottlesCount, String ticketNumber);
    void saveTicket(Ticket ticket);
    String generateUniqueTicketNumber();
    void saveBottlesCount(int totalBottlesCount);
    Optional<Ticket> getTicket(Integer id);
}