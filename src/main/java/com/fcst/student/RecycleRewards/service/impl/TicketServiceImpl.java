package com.fcst.student.RecycleRewards.service.impl;

import com.fcst.student.RecycleRewards.model.Ticket;
import com.fcst.student.RecycleRewards.repository.TicketRepository;
import com.fcst.student.RecycleRewards.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service

public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public Integer getDefaultBottlesCount(Integer bottlesCount) {
        return bottlesCount != null ? bottlesCount : 0;
    }

    @Override
    public Ticket createTicket(Integer bottlesCount, String ticketNumber) {
        if (ticketNumber == null || ticketNumber.isEmpty()) {
            ticketNumber = generateUniqueTicketNumber();
        }
        LocalDateTime issuedOn = LocalDateTime.now();
        LocalDateTime expirationDateTime = issuedOn.plusHours(72);

        Ticket ticket = new Ticket(ticketNumber, issuedOn, expirationDateTime, bottlesCount);
        // ticketRepository.save(ticket);

        return ticket;
    }


    @Override
    public void saveTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    @Override
    public void saveBottlesCount(int totalBottlesCount) {
        Ticket ticket = new Ticket(); // Or retrieve an existing ticket from the database
        ticket.setPoints(totalBottlesCount);
        ticketRepository.save(ticket);
    }

    @Override
    public Optional<Ticket> getTicket(Integer id) {
        return this.ticketRepository.findById(Long.valueOf(id));
    }

    public String generateUniqueTicketNumber() {
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