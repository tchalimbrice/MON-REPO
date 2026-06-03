package com.bizmaster.service.template.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

public class CollaboratorDto {
    private Long id;

    @NotBlank
    private Long companyId;

    @NotBlank
    private Long userId;

    @NotBlank
    private String position;

    private String status;  // PENDING, ACTIVE, SUSPENDED, REMOVED

    private boolean dataAccess = true;
    private boolean reportAccess = true;
    private boolean settingsAccess = false;

    private String notes;
    private Instant dateJoined;
    private Instant dateLeft;
    private Instant createdAt;
    private Instant updatedAt;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isDataAccess() {
        return dataAccess;
    }

    public void setDataAccess(boolean dataAccess) {
        this.dataAccess = dataAccess;
    }

    public boolean isReportAccess() {
        return reportAccess;
    }

    public void setReportAccess(boolean reportAccess) {
        this.reportAccess = reportAccess;
    }

    public boolean isSettingsAccess() {
        return settingsAccess;
    }

    public void setSettingsAccess(boolean settingsAccess) {
        this.settingsAccess = settingsAccess;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Instant dateJoined) {
        this.dateJoined = dateJoined;
    }

    public Instant getDateLeft() {
        return dateLeft;
    }

    public void setDateLeft(Instant dateLeft) {
        this.dateLeft = dateLeft;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
