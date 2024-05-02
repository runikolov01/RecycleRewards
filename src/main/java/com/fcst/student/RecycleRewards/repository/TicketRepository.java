package com.fcst.student.RecycleRewards.repository;

import com.fcst.student.RecycleRewards.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Ticket findByNumber(String number);
    boolean existsByNumber(String number);

}
