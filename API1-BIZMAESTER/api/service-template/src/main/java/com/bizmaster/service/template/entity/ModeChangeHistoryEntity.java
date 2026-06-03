package com.bizmaster.service.template.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "mode_change_history")
public class ModeChangeHistoryEntity {

    public enum ChangeStatus {
        INITIATED,    // Changement initié
        IN_PROGRESS,  // En cours
        COMPLETED,    // Complété
        FAILED        // Échoué
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyEntity company;

    @Column(nullable = false)
    private String oldMode;  // "CENTRALIZED" ou "COLLABORATIVE"

    @Column(nullable = false)
    private String newMode;  // "CENTRALIZED" ou "COLLABORATIVE"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChangeStatus status;

    @ManyToOne
    @JoinColumn(name = "initiated_by_user_id", nullable = false)
    private UserEntity initiatedByUser;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private int collaboratorsAdded = 0;
    private int dataRecordsTransferred = 0;

    private Instant completedAt;
    private Instant createdAt;
    private Instant updatedAt;

    // Constructors
    public ModeChangeHistoryEntity() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public ModeChangeHistoryEntity(CompanyEntity company, String oldMode, String newMode, UserEntity initiatedByUser) {
        this.company = company;
        this.oldMode = oldMode;
        this.newMode = newMode;
        this.initiatedByUser = initiatedByUser;
        this.status = ChangeStatus.INITIATED;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
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

    public ChangeStatus getStatus() {
        return status;
    }

    public void setStatus(ChangeStatus status) {
        this.status = status;
    }

    public UserEntity getInitiatedByUser() {
        return initiatedByUser;
    }

    public void setInitiatedByUser(UserEntity initiatedByUser) {
        this.initiatedByUser = initiatedByUser;
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
