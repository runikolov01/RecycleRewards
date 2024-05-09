package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.service.MachineService;
import com.fcst.student.RecycleRewards.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MachineController {
    private UserService userService;

    @Autowired
    private MachineService machineService;

    public MachineController(UserService userService, MachineService machineService) {
        this.userService = userService;
        this.machineService = machineService;
    }


    @GetMapping("/machines")
    public String showMachines(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean loggedIn = authentication != null && authentication.isAuthenticated();

        model.addAttribute("loggedIn", loggedIn);

        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            User user = userService.getUserById(userId);
            if (user != null) {
                model.addAttribute("loggedUser", user);

                Integer totalPoints = user.getTotalPoints();
                model.addAttribute("totalPoints", totalPoints);
            }
            System.out.println(user.getRole());
        }
        return "machines";
    }
}