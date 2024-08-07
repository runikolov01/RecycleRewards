package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.ReportDetails;
import com.fcst.student.RecycleRewards.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public String showReportsPage(Model model) {
        List<ReportDetails> reports = reportService.getAllReports();
        model.addAttribute("reports", reports);
        return "reports";
    }

    @PostMapping
    public String createReport(@RequestParam("startDate") String startDateStr,
                               @RequestParam("endDate") String endDateStr) {
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        reportService.createReport(startDate, endDate);
        return "redirect:/reports";
    }

    @GetMapping("/{id}")
    public String viewReport(@PathVariable Long id, Model model) {
        ReportDetails report = reportService.getReportById(id);
        model.addAttribute("report", report);
        return "report_details"; // Thymeleaf template name
    }

    @PostMapping("/delete/{id}")
    public String deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return "redirect:/reports";
    }
}