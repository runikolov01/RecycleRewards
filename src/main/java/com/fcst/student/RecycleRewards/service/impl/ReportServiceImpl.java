package com.fcst.student.RecycleRewards.service.impl;

import com.fcst.student.RecycleRewards.model.ReportDetails;
import com.fcst.student.RecycleRewards.service.ReportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final RestTemplate restTemplate;

    @Value("${spring.reports.service.url}")
    private String reportsServiceUrl;

    public ReportServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void createReport(LocalDate startDate, LocalDate endDate) {
        String url = reportsServiceUrl;
        restTemplate.postForObject(url, new CreateReportRequest(startDate, endDate), Void.class);
    }

    @Override
    public List<ReportDetails> getAllReports() {
        ResponseEntity<ReportDetails[]> response = restTemplate.getForEntity(reportsServiceUrl, ReportDetails[].class);
        return List.of(response.getBody());
    }

    @Override
    public ReportDetails getReportById(Long id) {
        String url = reportsServiceUrl + "/" + id;
        return restTemplate.getForObject(url, ReportDetails.class);
    }

    @Override
    public void deleteReport(Long id) {
        String url = reportsServiceUrl + "/" + id;
        restTemplate.delete(url);
    }

    private static class CreateReportRequest {
        private LocalDate startDate;
        private LocalDate endDate;

        public CreateReportRequest(LocalDate startDate, LocalDate endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }
    }

}