package com.fcst.student.RecycleRewards.repository;

import com.fcst.student.RecycleRewards.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
