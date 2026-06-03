package com.bizmaster.service.template.dto;

import java.time.Instant;

public class ModeChangeHistoryDto {
    private Long id;
    private Long companyId;
    private String oldMode;
    private String newMode;
    private String status;  // INITIATED, IN_PROGRESS, COMPLETED, FAILED
    private Long initiatedByUserId;
    private String notes;
    private int collaboratorsAdded;
    private int dataRecordsTransferred;
    private Instant completedAt;
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

    public String getOldMode() {
        return oldMode;
    }

    public void setOldMode(String oldMode) {
        this.oldMode = oldMode;
    }

    public String getNewMode() {
        return newMode;
    }

    public void setNewMode(String newMode) {
        this.newMode = newMode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getInitiatedByUserId() {
        return initiatedByUserId;
    }

    public void setInitiatedByUserId(Long initiatedByUserId) {
        this.initiatedByUserId = initiatedByUserId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getCollaboratorsAdded() {
        return collaboratorsAdded;
    }

    public void setCollaboratorsAdded(int collaboratorsAdded) {
        this.collaboratorsAdded = collaboratorsAdded;
    }

    public int getDataRecordsTransferred() {
        return dataRecordsTransferred;
    }

    public void setDataRecordsTransferred(int dataRecordsTransferred) {
        this.dataRecordsTransferred = dataRecordsTransferred;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
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
