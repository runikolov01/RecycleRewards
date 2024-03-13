package com.fcst.student.RecycleRewards.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class StartController {

    @GetMapping("/start")
    public String openStartPage(HttpSession session, Model model) {
        Integer bottlesCount = (Integer) session.getAttribute("bottlesCount");
        if (bottlesCount == null) {
            bottlesCount = 0;
        }
        model.addAttribute("bottlesCount", bottlesCount);
        return "start";
    }

    @PostMapping("/start")
    public String addBottle(HttpSession session) {
        Integer bottlesCount = (Integer) session.getAttribute("bottlesCount");
        if (bottlesCount == null) {
            bottlesCount = 0;
        }
        bottlesCount++;
        session.setAttribute("bottlesCount", bottlesCount);


       return "redirect:/start";
    }
}