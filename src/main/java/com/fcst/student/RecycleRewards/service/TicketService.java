package com.fcst.student.RecycleRewards.service;

import com.fcst.student.RecycleRewards.model.Ticket;
import org.springframework.stereotype.Service;

@Service
public interface TicketService {
    Integer getDefaultBottlesCount(Integer bottlesCount);
    Ticket createTicket(Integer bottlesCount);
}