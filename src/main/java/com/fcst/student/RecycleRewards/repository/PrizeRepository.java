package com.fcst.student.RecycleRewards.repository;

import com.fcst.student.RecycleRewards.model.Prize;
import com.fcst.student.RecycleRewards.model.enums.PrizeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PrizeRepository extends JpaRepository<Prize, Long> {
    @Query(value = "SELECT * FROM Prizes WHERE id NOT IN (SELECT DISTINCT prize_id FROM prize_winners) AND type = 'RAFFLE'", nativeQuery = true)
    List<Prize> findRafflePrizesWithoutWinners();

    @Query("SELECT u.id, p.id FROM User u JOIN u.prizes p")
    List<Object[]> findAllWonPrizes();

    List<Prize> findByTypeAndStartDateBefore(PrizeType type, LocalDateTime date);

    List<Prize> findByRemainedTicketsGreaterThanAndStartDateBefore(Integer tickets, LocalDateTime date);

    List<Prize> findByTypeAndRemainedTicketsGreaterThanAndStartDateBefore(PrizeType type, Integer tickets, LocalDateTime date);
}