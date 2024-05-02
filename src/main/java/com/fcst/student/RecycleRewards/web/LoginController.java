package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.service.UserService;
import com.fcst.student.RecycleRewards.service.session.LoggedUser;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final UserService userService;
    private final LoggedUser loggedUser;
    private final ModelMapper modelMapper;

    @Autowired
    public LoginController(UserService userService, LoggedUser loggedUser, ModelMapper modelMapper) {
        this.userService = userService;
        this.loggedUser = loggedUser;
        this.modelMapper = modelMapper;
    }

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
            // Authentication successful, redirect to dashboard/homepage
            loggedUser.setLoggedEmail(user.getEmail());
            loggedUser.setLoggedFirstName(user.getFirstName());
            loggedUser.setLoggedLastName(user.getLastName());
            loggedUser.setLoggedPassword(user.getPassword());
            return "redirect:/success";
        } else {
            // Authentication failed, redirect back to login page with error message
            model.addAttribute("error", true);
            return "redirect:/login?error";
        }
    }

    @GetMapping("/success")
    public String successPage(Model model) {
        model.addAttribute("loggedUser", loggedUser);
        return "success";
    }
}