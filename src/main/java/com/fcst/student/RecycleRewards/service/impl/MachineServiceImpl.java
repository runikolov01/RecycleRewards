package com.fcst.student.RecycleRewards.service.impl;

import com.fcst.student.RecycleRewards.model.Machine;
import com.fcst.student.RecycleRewards.repository.MachineRepository;
import com.fcst.student.RecycleRewards.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MachineServiceImpl implements MachineService {
    @Autowired
    private MachineRepository machineRepository;

    @Override
    public void saveMachine(Machine machine) {
        machineRepository.save(machine);
    }

    @Override
    public void deleteMachine(Long machineId) {
        machineRepository.deleteById(machineId);
    }

    @Override
    public Machine updateMachine(Machine machine) {
        return machineRepository.save(machine);
    }

    @Override
    public Machine getMachineById(Long machineId) {
        return machineRepository.getById(machineId);
    }

    @Override
    public List<Machine> getAllMachines() {
        return List.of();
    }
}