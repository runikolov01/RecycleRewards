package com.fcst.student.RecycleRewards.repository;

import com.fcst.student.RecycleRewards.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    boolean existsByNumber(String number);

}
