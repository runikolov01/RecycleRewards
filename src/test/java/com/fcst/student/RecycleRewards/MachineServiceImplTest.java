package com.fcst.student.RecycleRewards;


import com.fcst.student.RecycleRewards.model.Machine;
import com.fcst.student.RecycleRewards.repository.MachineRepository;
import com.fcst.student.RecycleRewards.service.impl.MachineServiceImpl;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class MachineServiceImplTest {

    @Mock
    private MachineRepository machineRepository;

    @Mock
    private Dotenv dotenv;

    @InjectMocks
    private MachineServiceImpl machineService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllMachines() {
        Machine machine1 = new Machine(1L, "Location 1", null, "Barcode1");
        Machine machine2 = new Machine(2L, "Location 2", null, "Barcode2");
        List<Machine> machines = Arrays.asList(machine1, machine2);

        when(machineRepository.findAll()).thenReturn(machines);

        List<Machine> result = machineService.getAllMachines();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Location 1", result.get(0).getLocationName());
    }

    @Test
    void testPopulateModelWithMachinesAndApiKey() {
        Machine machine1 = new Machine(1L, "Location 1", null, "Barcode1");
        List<Machine> machines = Arrays.asList(machine1);

        when(machineRepository.findAll()).thenReturn(machines);
        when(dotenv.get("GOOGLE_MAPS_API_KEY")).thenReturn("fake-api-key");

        Model model = mock(Model.class);

        machineService.populateModelWithMachinesAndApiKey(model);

        verify(model).addAttribute("machines", machines);
        verify(model).addAttribute("googleMapsApiKey", "fake-api-key");
    }
}
