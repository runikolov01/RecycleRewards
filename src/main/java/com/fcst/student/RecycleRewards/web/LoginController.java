package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.service.UserService;
import com.fcst.student.RecycleRewards.service.session.LoggedUser;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
    public String loginSubmit(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        User user = userService.getUserByEmail(email);
        if (user != null && userService.verifyPassword(user, password)) {
            // Authentication successful, store user ID in session
            session.setAttribute("userId", user.getId());
            return "redirect:/myProfile";
        } else {
            // Authentication failed, redirect back to login page with error message
            model.addAttribute("error", true);
            return "redirect:/login?error";
        }
    }

    @GetMapping("/myProfile")
    public String openMyProfile(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            // Fetch user details from the database using the user ID
            User user = userService.getUserById(userId);
            if (user != null) {
                model.addAttribute("loggedUser", user);
                return "myProfile";
            }
        }
        // Handle case when user ID is not found or user is not found
        return "redirect:/login"; // or any other appropriate action
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("userId");
        this.userService.logout();
        session.invalidate();
        return "redirect:/home";
    }
}
