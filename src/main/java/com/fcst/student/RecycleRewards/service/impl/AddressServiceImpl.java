package com.fcst.student.RecycleRewards.service.impl;

import com.fcst.student.RecycleRewards.model.Address;
import com.fcst.student.RecycleRewards.repository.AddressRepository;
import com.fcst.student.RecycleRewards.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public void saveAddress(Address address) {
        addressRepository.save(address);
    }
}
