package com.fcst.student.RecycleRewards;

import com.fcst.student.RecycleRewards.model.Address;
import com.fcst.student.RecycleRewards.model.Machine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MachineTest {
    private Machine machine;
    private Address address;

    @BeforeEach
    public void setUp() {
        address = new Address(1L, "City", 12345, "Street", 10, 1, 101);
        machine = new Machine();
    }

    @Test
    public void testDefaultConstructor() {
        assertNull(machine.getId());
        assertNull(machine.getLocationName());
        assertNull(machine.getAddress());
        assertNull(machine.getBarcode());
    }

    @Test
    public void testParameterizedConstructor() {
        // Initialize Machine using the parameterized constructor
        machine = new Machine(1L, "LocationName", address, "Barcode123");

        // Verify that the parameterized constructor sets fields correctly
        assertEquals(1L, machine.getId());
        assertEquals("LocationName", machine.getLocationName());
        assertEquals(address, machine.getAddress());
        assertEquals("Barcode123", machine.getBarcode());
    }

    @Test
    public void testSetId() {
        machine.setId(2L);
        assertEquals(2L, machine.getId());
    }

    @Test
    public void testSetLocationName() {
        machine.setLocationName("NewLocation");
        assertEquals("NewLocation", machine.getLocationName());
    }

    @Test
    public void testSetAddress() {
        Address newAddress = new Address(2L, "NewCity", 67890, "NewStreet", 20, 2, 202);
        machine.setAddress(newAddress);
        assertEquals(newAddress, machine.getAddress());
    }

    @Test
    public void testSetBarcode() {
        machine.setBarcode("NewBarcode");
        assertEquals("NewBarcode", machine.getBarcode());
    }
}