package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginForm(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", true);
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String email, @RequestParam String password, Model model) {
        User user = userService.getUserByEmail(email);
        if (user != null && userService.verifyPassword(user, password)) {
            // Authentication successful
            return "redirect:/success"; // Redirect to dashboard/homepage
        } else {
            // Authentication failed
            model.addAttribute("error", true);
            return "redirect:/login?error"; // Redirect back to login page with error message
        }
    }

    @GetMapping("/success")
    public String successPage() {
        return "success";
    }
}