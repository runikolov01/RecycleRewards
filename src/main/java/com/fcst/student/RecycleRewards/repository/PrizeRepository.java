package com.fcst.student.RecycleRewards.repository;

import com.fcst.student.RecycleRewards.model.Prize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrizeRepository extends JpaRepository<Long, Prize> {
}
