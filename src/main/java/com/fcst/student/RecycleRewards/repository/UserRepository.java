package com.fcst.student.RecycleRewards.repository;

import com.fcst.student.RecycleRewards.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    User findByEmail(String email);

    @Query("SELECT p.user FROM Purchase p WHERE p.prize.id = :prizeId")
    List<User> findAllByPrizeId(@Param("prizeId") Long prizeId);

    @Query("SELECT u FROM User u JOIN FETCH u.prizes")
    List<User> findUsersWithPrizes();

    @Query("SELECT SUM(u.totalBottles) FROM User u")
    Integer getTotalBottlesSum();
}