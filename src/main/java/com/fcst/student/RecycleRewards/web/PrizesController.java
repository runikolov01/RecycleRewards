package com.fcst.student.RecycleRewards.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PrizesController {
    @GetMapping("/prizes")
    public String openPrizesPage() {
        return "prizes";
    }
}
