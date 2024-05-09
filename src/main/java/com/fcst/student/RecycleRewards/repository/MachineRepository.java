package com.fcst.student.RecycleRewards.repository;

import com.fcst.student.RecycleRewards.model.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {
}
