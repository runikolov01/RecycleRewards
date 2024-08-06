package com.fcst.student.RecycleRewards.UnitTests;

import com.fcst.student.RecycleRewards.service.MachineService;
import com.fcst.student.RecycleRewards.service.UserService;
import com.fcst.student.RecycleRewards.service.impl.PrizeServiceImpl;
import com.fcst.student.RecycleRewards.web.MachineController;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MachineControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private MachineService machineService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private MachineController machineController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowMachines() {
        doNothing().when(machineService).populateModelWithMachinesAndApiKey(model);

        String viewName = machineController.showMachines(model, session);

        verify(model, times(1)).addAttribute(anyString(), any());
        verify(machineService, times(1)).populateModelWithMachinesAndApiKey(model);
        assertEquals("machines", viewName);
    }
}