package com.fcst.student.RecycleRewards.service;

import com.fcst.student.RecycleRewards.model.ReportDetails;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    void createReport(LocalDate startDate, LocalDate endDate);

    List<ReportDetails> getAllReports();

    ReportDetails getReportById(Long id);

    void deleteReport(Long id);
}