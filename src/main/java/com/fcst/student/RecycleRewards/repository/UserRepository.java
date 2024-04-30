package com.fcst.student.RecycleRewards.repository;

import com.fcst.student.RecycleRewards.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Person, Long> {
    boolean existsByEmail(String email);
}
