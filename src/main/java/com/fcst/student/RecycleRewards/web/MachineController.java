package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.service.MachineService;
import com.fcst.student.RecycleRewards.service.UserService;
import com.fcst.student.RecycleRewards.service.impl.PrizeServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MachineController {
    private final UserService userService;
    private final MachineService machineService;

    @Autowired
    public MachineController(UserService userService, MachineService machineService) {
        this.userService = userService;
        this.machineService = machineService;
    }

    @GetMapping("/machines")
    public String showMachines(Model model, HttpSession session) {
        PrizeServiceImpl.setLoggedInAttribute(model, session, userService);
        machineService.populateModelWithMachinesAndApiKey(model);
        return "machines";
    }
}