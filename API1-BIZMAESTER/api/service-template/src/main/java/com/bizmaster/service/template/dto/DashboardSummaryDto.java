package com.bizmaster.service.template.dto;

import java.util.List;

public class DashboardSummaryDto {
    private String service;
    private List<DashboardMetricDto> metrics;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public List<DashboardMetricDto> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<DashboardMetricDto> metrics) {
        this.metrics = metrics;
    }
}
