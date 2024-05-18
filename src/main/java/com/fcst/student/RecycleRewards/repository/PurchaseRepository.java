package com.fcst.student.RecycleRewards.repository;

import com.fcst.student.RecycleRewards.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
