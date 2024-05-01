package com.fcst.student.RecycleRewards.repository;

import com.fcst.student.RecycleRewards.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    User findByEmail(String email); // Add this method to find a person by email

}
