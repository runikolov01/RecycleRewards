package com.fcst.student.RecycleRewards.service;

import com.fcst.student.RecycleRewards.model.Address;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {
    void saveAddress(Address address);
}
