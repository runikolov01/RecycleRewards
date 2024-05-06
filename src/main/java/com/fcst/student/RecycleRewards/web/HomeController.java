package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.repository.TicketRepository;
import com.fcst.student.RecycleRewards.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserService userService;
    private final TicketRepository ticketRepository;


    @Autowired
    public HomeController(UserService userService, TicketRepository ticketRepository) {
        this.userService = userService;
        this.ticketRepository = ticketRepository;
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        // Check if the user is logged in
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean loggedIn = authentication != null && authentication.isAuthenticated();

        // Add the loggedIn attribute to the model
        model.addAttribute("loggedIn", loggedIn);

        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            User user = userService.getUserById(userId);
            if (user != null) {
                model.addAttribute("loggedUser", user);

                // Fetch totalPoints for the logged-in user and add it to the model
                Integer totalPoints = user.getTotalPoints();
                model.addAttribute("totalPoints", totalPoints);
            }
            System.out.println(user.getRole());
        }
        return "home";
    }


    @GetMapping("/about")
    public String about(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            // Fetch user details from the database using the user ID
            User user = userService.getUserById(userId);
            if (user != null) {
                model.addAttribute("loggedUser", user);
                Integer totalPoints = user.getTotalPoints();
                model.addAttribute("totalPoints", totalPoints);
            }
        }
        return "about";
    }
}