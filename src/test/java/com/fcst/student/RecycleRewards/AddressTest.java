package com.fcst.student.RecycleRewards;

import com.fcst.student.RecycleRewards.model.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddressTest {

    private Address address;

    @BeforeEach
    public void setUp() {
        address = new Address();
    }

    @Test
    public void testSetStreetNumberWithInt() {
        int streetNumber = 123;
        address.setStreetNumber(streetNumber);
        assertEquals(streetNumber, address.getStreetNumber());
    }

    @Test
    public void testSetStreetNumberWithInteger() {
        Integer streetNumber = 456;
        address.setStreetNumber(streetNumber);
        assertEquals(streetNumber, address.getStreetNumber());
    }

    @Test
    public void testSetFloorWithInt() {
        int floor = 5;
        address.setFloor(floor);
        assertEquals(floor, address.getFloor());
    }

    @Test
    public void testSetFloorWithInteger() {
        Integer floor = 10;
        address.setFloor(floor);
        assertEquals(floor, address.getFloor());
    }

    @Test
    public void testNoArgsConstructor() {
        Address address = new Address();
        assertNotNull(address);
    }

    @Test
    public void testAllArgsConstructor() {
        Long id = 1L;
        String city = "Sample City";
        int postcode = 12345;
        String street = "Sample Street";
        Integer streetNumber = 100;
        Integer floor = 2;
        Integer apartmentNumber = 202;

        Address address = new Address(id, city, postcode, street, streetNumber, floor, apartmentNumber);

        assertEquals(id, address.getId());
        assertEquals(city, address.getCity());
        assertEquals(postcode, address.getPostcode());
        assertEquals(street, address.getStreet());
        assertEquals(streetNumber, address.getStreetNumber());
        assertEquals(floor, address.getFloor());
        assertEquals(apartmentNumber, address.getApartmentNumber());
    }

    @Test
    public void testPartialArgsConstructor() {
        Long id = 1L;
        String city = "Sample City";
        int postcode = 12345;
        String street = "Sample Street";
        Integer streetNumber = 100;

        Address address = new Address(id, city, postcode, street, streetNumber);

        assertEquals(id, address.getId());
        assertEquals(city, address.getCity());
        assertEquals(postcode, address.getPostcode());
        assertEquals(street, address.getStreet());
        assertEquals(streetNumber, address.getStreetNumber());
        assertNull(address.getFloor());
        assertNull(address.getApartmentNumber());
    }

    @Test
    public void testGettersAndSetters() {
        Long id = 1L;
        String city = "Sample City";
        int postcode = 12345;
        String street = "Sample Street";
        Integer streetNumber = 100;
        Integer floor = 2;
        Integer apartmentNumber = 202;

        Address address = new Address();

        address.setId(id);
        address.setCity(city);
        address.setPostcode(postcode);
        address.setStreet(street);
        address.setStreetNumber(streetNumber);
        address.setFloor(floor);
        address.setApartmentNumber(apartmentNumber);

        assertEquals(id, address.getId());
        assertEquals(city, address.getCity());
        assertEquals(postcode, address.getPostcode());
        assertEquals(street, address.getStreet());
        assertEquals(streetNumber, address.getStreetNumber());
        assertEquals(floor, address.getFloor());
        assertEquals(apartmentNumber, address.getApartmentNumber());
    }

    @Test
    public void testStreetNumberGetterSetter() {
        Address address = new Address();
        Integer streetNumber = 100;

        address.setStreetNumber(streetNumber);
        assertEquals(streetNumber, address.getStreetNumber());
    }

    @Test
    public void testFloorGetterSetter() {
        Address address = new Address();
        Integer floor = 2;

        address.setFloor(floor);
        assertEquals(floor, address.getFloor());
    }

    @Test
    public void testApartmentNumberGetterSetter() {
        Address address = new Address();
        Integer apartmentNumber = 202;

        address.setApartmentNumber(apartmentNumber);
        assertEquals(apartmentNumber, address.getApartmentNumber());
    }
}