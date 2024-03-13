package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.service.TicketService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class StartController {
    @Autowired
    private TicketService ticketService;

    @GetMapping("/start")
    public String openStartPage(HttpSession session, Model model) {
        Integer bottlesCount = (Integer) session.getAttribute("bottlesCount");
        bottlesCount = ticketService.getDefaultBottlesCount(bottlesCount);

        model.addAttribute("bottlesCount", bottlesCount);
        return "start";
    }

    @PostMapping("/start")
    public String addBottle(HttpSession session) {
        Integer bottlesCount = (Integer) session.getAttribute("bottlesCount");
        bottlesCount = ticketService.getDefaultBottlesCount(bottlesCount);
        bottlesCount++;

        session.setAttribute("bottlesCount", bottlesCount);

       return "redirect:/start";
    }
}