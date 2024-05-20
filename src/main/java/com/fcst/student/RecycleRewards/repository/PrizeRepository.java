package com.fcst.student.RecycleRewards.repository;

import com.fcst.student.RecycleRewards.model.Prize;
import com.fcst.student.RecycleRewards.model.enums.PrizeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrizeRepository extends JpaRepository<Prize, Long> {
    List<Prize> findByType(PrizeType type);


    @Query(value = "SELECT * FROM Prizes WHERE id NOT IN (SELECT DISTINCT prize_id FROM person_won_prizes)", nativeQuery = true)
    List<Prize> findPrizesWithoutWinners();

    List<Prize> findByTypeAndRemainedTicketsGreaterThan(PrizeType prizeType, int remainedTickets);

    List<Prize> findByRemainedTicketsGreaterThan(int remainedTickets);


    @Query("SELECT p FROM Prize p " +
            "JOIN p.participants u " +
            "WHERE u.id IN (SELECT pw.id FROM User pw JOIN pw.prizes pr WHERE pr.id = p.id)")
    List<Prize> findAllWonPrizes();




}
