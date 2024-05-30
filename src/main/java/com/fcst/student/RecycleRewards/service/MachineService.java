package com.fcst.student.RecycleRewards.service;

import com.fcst.student.RecycleRewards.model.Machine;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public interface MachineService {
    List<Machine> getAllMachines();

    void populateModelWithMachinesAndApiKey(Model model);
}