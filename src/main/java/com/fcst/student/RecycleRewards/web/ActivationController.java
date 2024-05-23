package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class ActivationController {

    @Autowired
    private UserService userService;

    @GetMapping("/activate")
    public String activateAccount(@RequestParam String token, RedirectAttributes redirectAttributes) {
        User user = userService.findByActivationToken(token);

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid activation token");
            return "redirect:/login";
        }

        if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("error", "Activation token has expired");
            return "redirect:/login";
        }

        user.setActivated(true);
        user.setActivationToken(null); // Remove the activation token after successful activation
        user.setTokenExpiry(null); // Remove the token expiry after successful activation
        userService.saveUser(user);

        redirectAttributes.addFlashAttribute("success", "Account activated successfully");
        return "redirect:/login";
    }
}
