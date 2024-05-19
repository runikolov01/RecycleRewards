package com.fcst.student.RecycleRewards.repository;

import com.fcst.student.RecycleRewards.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    User findByEmail(String email);

//    @Query("SELECT u FROM User u JOIN u.prizes p WHERE p.id = :prizeId")
//    List<User> findParticipantsByPrizeId(@Param("prizeId") Long prizeId);

    List<User> findAllById(Long prizeId);
}