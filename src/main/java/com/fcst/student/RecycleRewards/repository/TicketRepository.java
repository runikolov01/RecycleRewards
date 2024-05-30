package com.fcst.student.RecycleRewards.repository;

import com.fcst.student.RecycleRewards.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByNumber(String number);

    boolean existsByNumber(String number);
}