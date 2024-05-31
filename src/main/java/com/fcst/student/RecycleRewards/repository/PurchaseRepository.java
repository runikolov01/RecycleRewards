package com.fcst.student.RecycleRewards.repository;

import com.fcst.student.RecycleRewards.model.PrizeDetailsDto;
import com.fcst.student.RecycleRewards.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByUserId(Long userId);

    List<Purchase> findAllByPrizeId(Long prizeId);

    boolean existsByWinnerCode(String number);

    @Query("SELECT new com.fcst.student.RecycleRewards.model.PrizeDetailsDto(p.prize.id, p.prize.name, p.prize.description, p.winnerCode) " +
            "FROM Purchase p " +
            "JOIN p.user u " +
            "JOIN p.prize pr " +
            "WHERE u.id = :userId AND p.winnerCode IS NOT NULL")
    List<PrizeDetailsDto> findPrizeDetailsByUserId(@Param("userId") Long userId);
}
