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
            redirectAttributes.addFlashAttribute("error", "Вече сте активирали своя профил!");
            return "redirect:/activated_profile";
        }

        if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            userService.deleteUser(user.getId());
            redirectAttributes.addFlashAttribute("linkExpired", "Линкът е изтекъл! Изминали са повече от 24 часа от получаването на този email.");
            return "redirect:/activated_profile";
        }

        user.setActivated(true);
        user.setActivationToken(null);
        user.setTokenExpiry(null);
        userService.saveUser(user);

        redirectAttributes.addFlashAttribute("success", "Профилът е активиран успешно!");
        return "redirect:/activated_profile";
    }

    @GetMapping("/activated_profile")
    public String showFailedActivation() {
        return "activated_profile";
    }
}