package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.Machine;
import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.service.MachineService;
import com.fcst.student.RecycleRewards.service.UserService;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MachineController {

    @Autowired
    private Dotenv dotenv;

    @Autowired
    private UserService userService;

    @Autowired
    private MachineService machineService;

    @GetMapping("/machines")
    public String showMachines(Model model, HttpSession session) {
        Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
        if (loggedIn == null) {
            loggedIn = false;
        }
        session.setAttribute("loggedIn", loggedIn);
        model.addAttribute("loggedIn", loggedIn);

        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            User user = userService.getUserById(userId);
            if (user != null) {
                session.setAttribute("loggedUser", user);
                model.addAttribute("loggedUser", user);

                Integer totalPoints = user.getTotalPoints();
                session.setAttribute("totalPoints", totalPoints);
                model.addAttribute("totalPoints", totalPoints);
            }
        }

        List<Machine> machines = machineService.getAllMachines();
        model.addAttribute("machines", machines);

        // Add the Google Maps API key to the model
        String googleMapsApiKey = dotenv.get("GOOGLE_MAPS_API_KEY");
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);

        return "machines";
    }
}
