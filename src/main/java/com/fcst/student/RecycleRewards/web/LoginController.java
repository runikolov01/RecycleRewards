package com.fcst.student.RecycleRewards.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginForm(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", true);
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String username, @RequestParam String password) {
        // For simplicity, let's just check if the username and password are not empty
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            // Redirect to success page if login is successful
            return "redirect:/success";
        } else {
            // Redirect back to login page with error message if login fails
            return "redirect:/login?error";
        }
    }

    @GetMapping("/success")
    public String successPage() {
        return "success";
    }
}
