package com.fcst.student.RecycleRewards.service;

import com.fcst.student.RecycleRewards.model.Machine;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MachineService {
    void saveMachine(Machine machine);

    void deleteMachine(Long machineId);

    Machine updateMachine(Machine machine);

    Machine getMachineById(Long machineId);

    List<Machine> getAllMachines();
}
