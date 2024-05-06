package com.fcst.student.RecycleRewards.service;

import com.fcst.student.RecycleRewards.model.Address;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AddressService {
    void saveAddress(Address address);

    List<Address> getAddresses();

    Address getAddressById(int id);

    void deleteAddressById(int id);
}
