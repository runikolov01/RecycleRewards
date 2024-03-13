package com.fcst.student.RecycleRewards.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FinishController {
    @GetMapping("/finish")
    public String openFinishPage() {
        return "finish";
    }
}
