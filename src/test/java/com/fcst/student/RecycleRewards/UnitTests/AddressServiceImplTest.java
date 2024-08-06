package com.fcst.student.RecycleRewards.UnitTests;

import com.fcst.student.RecycleRewards.model.Address;
import com.fcst.student.RecycleRewards.repository.AddressRepository;
import com.fcst.student.RecycleRewards.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveAddress() {
        Address address = new Address();

        addressService.saveAddress(address);

        verify(addressRepository, times(1)).save(address);
    }
}
