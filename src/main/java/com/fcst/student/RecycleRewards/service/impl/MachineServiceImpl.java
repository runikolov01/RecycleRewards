package com.fcst.student.RecycleRewards.service.impl;

import com.fcst.student.RecycleRewards.model.Machine;
import com.fcst.student.RecycleRewards.repository.MachineRepository;
import com.fcst.student.RecycleRewards.service.MachineService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class MachineServiceImpl implements MachineService {
    private final MachineRepository machineRepository;
    private final Dotenv dotenv;

    @Autowired
    public MachineServiceImpl(MachineRepository machineRepository, Dotenv dotenv) {
        this.machineRepository = machineRepository;
        this.dotenv = dotenv;
    }

    @Override
    public List<Machine> getAllMachines() {
        return machineRepository.findAll();
    }

    @Override
    public void populateModelWithMachinesAndApiKey(Model model) {
        List<Machine> machines = getAllMachines();
        model.addAttribute("machines", machines);

        String googleMapsApiKey = dotenv.get("GOOGLE_MAPS_API_KEY");
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);
    }
}