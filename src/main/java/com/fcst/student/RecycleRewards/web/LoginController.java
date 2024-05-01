package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginForm(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", true);
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String email, @RequestParam String password) {
        // Find the person with the given email
        User user = userRepository.findByEmail(email);

        // Check if a person with the given email exists and if the provided password matches the stored password
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
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