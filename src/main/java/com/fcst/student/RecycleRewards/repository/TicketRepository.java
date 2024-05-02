package com.fcst.student.RecycleRewards.repository;

import com.fcst.student.RecycleRewards.model.Ticket;
import com.fcst.student.RecycleRewards.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Ticket findByNumber(String number);

    boolean existsByNumber(String number);

    @Query("SELECT SUM(t.points) FROM Ticket t WHERE t.registeredBy = :user")
    Integer getTotalPointsByUser(@Param("user") User user);
}
