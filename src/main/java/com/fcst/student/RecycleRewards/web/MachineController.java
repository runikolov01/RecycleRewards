package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MachineController {
    @Autowired
    private MachineService machineService;

    @GetMapping("/machines")
    public String showMachines() {
        //return machineService.getAllMachines();
        return "machines";
    }
}